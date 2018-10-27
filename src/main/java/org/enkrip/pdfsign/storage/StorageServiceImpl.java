package org.enkrip.pdfsign.storage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.enkrip.pdfsign.PdfSignServerApplication;
import org.enkrip.pdfsign.hash.HashService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
class StorageServiceImpl implements StorageService {
	private final HashService hashService;
	private final File saveDirectory;

	StorageServiceImpl(PdfSignServerApplication.PDFSignProperties pdfSignProperties, ResourceLoader resourceLoader,
					   HashService hashService) throws IOException {
		this.hashService = hashService;
		this.saveDirectory = resourceLoader.getResource(pdfSignProperties.getStorageLocation()).getFile();
		if (!this.saveDirectory.exists()) {
			this.saveDirectory.mkdir();
		}
	}

	@Override
	public String saveFile(byte[] bytes) throws IOException {
		String hash = hashService.hash(bytes);
		if (getFile(hash).isPresent())
			return hash;
		File newFile = new File(saveDirectory, hash);
		if (newFile.createNewFile()) {
			FileUtils.writeByteArrayToFile(newFile, bytes);
		}
		return hash;
	}

	@Override
	public Optional<File> getFile(String hash) {
		File file = FileUtils.getFile(saveDirectory, hash);
		return Optional.of(file)
				.filter(File::exists);
	}
}
