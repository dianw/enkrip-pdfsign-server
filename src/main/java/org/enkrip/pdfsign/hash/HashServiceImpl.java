package org.enkrip.pdfsign.hash;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;
import org.enkrip.pdfsign.PdfSignServerApplication;
import org.springframework.stereotype.Service;

@Service
class HashServiceImpl implements HashService {
	private final PdfSignServerApplication.PDFSignProperties pdfSignProperties;

	public HashServiceImpl(PdfSignServerApplication.PDFSignProperties pdfSignProperties) {
		this.pdfSignProperties = pdfSignProperties;
	}

	@Override
	public String hash(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance(pdfSignProperties.getDefaultHashAlg());
			byte[] hash = digest.digest(bytes);
			return Hex.toHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}
}
