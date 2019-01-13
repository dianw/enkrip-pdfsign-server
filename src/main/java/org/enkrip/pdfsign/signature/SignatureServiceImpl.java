package org.enkrip.pdfsign.signature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

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
import org.jooq.lambda.Unchecked;
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

	private List<X509Certificate> getCertInfo(byte[] signatureContent) throws CMSException {
		CMSSignedData signedData = new CMSSignedData(signatureContent);
		Store<X509CertificateHolder> certificatesStore = signedData.getCertificates();
		Collection<X509CertificateHolder> matches = certificatesStore.getMatches(null);
		return matches.stream()
			.map(Unchecked.function(certConverter::getCertificate))
			.collect(Collectors.toList());
	}

	private List<SignatureRestData> getSignatures(byte[] bytes) throws IOException {
		try (PDDocument document = PDDocument.load(bytes)) {
			return document.getSignatureDictionaries()
				.stream()
				.filter(signature -> {
					COSBase type = signature.getCOSObject().getItem(COSName.TYPE);
					return type.equals(COSName.SIG) || type.equals(COSName.DOC_TIME_STAMP);
				})
				.map(Unchecked.function((PDSignature signature) -> {
					byte[] signatureContents = signature.getContents(bytes);
					List<X509Certificate> certificates = getCertInfo(signatureContents);
					return new AbstractMap.SimpleImmutableEntry<>(signature, certificates);
				}))
				.map(certs -> CERT_MAPPER.toRestData(certs.getKey(), new LinkedHashSet<>(certs.getValue())))
				.collect(Collectors.toList());
		}
	}

	@Override
	@Cacheable("signatures")
	public List<SignatureRestData> getSignatures(String hash) {
		return storageService.getFile(hash)
			.map(Unchecked.function((File file) -> {
				byte[] bytes = FileUtils.readFileToByteArray(file);
				return getSignatures(bytes);
			}))
			.orElseThrow(() -> new IllegalArgumentException(new FileNotFoundException()));
	}
}
