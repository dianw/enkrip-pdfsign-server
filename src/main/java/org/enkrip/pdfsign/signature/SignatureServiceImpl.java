package org.enkrip.pdfsign.signature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.util.Store;
import org.enkrip.pdfsign.storage.StorageService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
class SignatureServiceImpl implements SignatureService {
	private static final CertificateMapper CERT_MAPPER = CertificateMapper.MAPPER;
	private final JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
	private final StorageService storageService;

	SignatureServiceImpl(StorageService storageService) {
		this.storageService = storageService;
	}

	private List<X509Certificate> getCertInfo(byte[] signatureContent) throws CMSException, CertificateException {
		CMSSignedData signedData = new CMSSignedData(signatureContent);
		Store<X509CertificateHolder> certificatesStore = signedData.getCertificates();
		Collection<X509CertificateHolder> matches = certificatesStore.getMatches(null);
		List<X509Certificate> certificates = new ArrayList<>();
		for (X509CertificateHolder holder : matches) {
			certificates.add(certConverter.getCertificate(holder));
		}
		return Collections.unmodifiableList(certificates);
	}

	private List<SignatureRestData> getSignatures(byte[] bytes) throws IOException, CMSException, CertificateException {
		try (PDDocument document = PDDocument.load(bytes)) {
			List<SignatureRestData> signatures = new ArrayList<>();
			for (PDSignature signature : document.getSignatureDictionaries()) {
				COSBase type = signature.getCOSObject().getItem(COSName.TYPE);
				if (!(type.equals(COSName.SIG) || type.equals(COSName.DOC_TIME_STAMP))) continue;
				byte[] signatureContents = signature.getContents(bytes);
				List<X509Certificate> certificates = getCertInfo(signatureContents);
				if (!certificates.isEmpty()) {
					signatures.add(CERT_MAPPER.toRestData(signature, new LinkedHashSet<>(certificates)));
				}
			}
			return Collections.unmodifiableList(signatures);
		}
	}

	@Override
	@Cacheable("signatures")
	public List<SignatureRestData> getSignatures(String hash) throws IOException, CMSException, CertificateException {
		Optional<File> file = storageService.getFile(hash);
		if (!file.isPresent()) {
			throw new IllegalArgumentException(new FileNotFoundException());
		}
		byte[] bytes = FileUtils.readFileToByteArray(file.get());
		return getSignatures(bytes);
	}
}
