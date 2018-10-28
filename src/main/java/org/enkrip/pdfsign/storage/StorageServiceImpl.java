package org.enkrip.pdfsign.storage;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.util.encoders.Hex;
import org.enkrip.pdfsign.PdfSignServerApplication;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
class StorageServiceImpl implements StorageService {
	private final File saveDirectory;
	private final PdfSignServerApplication.PDFSignProperties pdfSignProperties;

	StorageServiceImpl(PdfSignServerApplication.PDFSignProperties pdfSignProperties,
					   ResourceLoader resourceLoader) throws IOException {
		this.pdfSignProperties = pdfSignProperties;
		this.saveDirectory = resourceLoader.getResource(pdfSignProperties.getStorageLocation()).getFile();
	}

	@Override
	public String generateHash(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance(pdfSignProperties.getDefaultHashAlg());
			byte[] hash = digest.digest(bytes);
			return Hex.toHexString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Optional<File> getFile(String hash) {
		File file = FileUtils.getFile(saveDirectory, hash);
		return Optional.of(file)
				.filter(File::exists);
	}

	@PostConstruct
	public void init() {
		if (!saveDirectory.exists()) {
			saveDirectory.mkdir();
		}
	}

	@Override
	public String saveFile(byte[] bytes) throws IOException {
		String hash = generateHash(bytes);
		if (getFile(hash).isPresent())
			return hash;
		File newFile = new File(saveDirectory, hash);
		if (newFile.createNewFile()) {
			FileUtils.writeByteArrayToFile(newFile, bytes);
		}
		return hash;
	}
}
