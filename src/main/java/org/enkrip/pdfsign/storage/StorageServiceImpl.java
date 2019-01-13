package org.enkrip.pdfsign.storage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.enkrip.pdfsign.PdfSignServerApplication;
import org.jooq.lambda.Unchecked;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
class StorageServiceImpl implements StorageService {
	private final File saveDirectory;

	StorageServiceImpl(PdfSignServerApplication.PDFSignProperties pdfSignProperties, ResourceLoader resourceLoader)
			throws IOException {
		this.saveDirectory = resourceLoader.getResource(pdfSignProperties.getStorageLocation()).getFile();
	}

	@Override
	public Optional<File> getFile(String hash) {
		File file = FileUtils.getFile(saveDirectory, hash);
		return Optional.of(file).filter(File::exists);
	}

	@PostConstruct
	public void init() {
		if (!saveDirectory.exists()) {
			saveDirectory.mkdir();
		}
	}

	@Override
	public String saveFile(byte[] bytes) {
		String hash = DigestUtils.sha256Hex(bytes);
		return getFile(hash)
			.map(file -> hash)
			.orElseGet(Unchecked.supplier(() -> {
				File newFile = new File(saveDirectory, hash);
				if (newFile.createNewFile()) {
					FileUtils.writeByteArrayToFile(newFile, bytes);
				}
				return hash;
			}));
	}
}
