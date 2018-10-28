package org.enkrip.pdfsign.storage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface StorageService {
	String generateHash(byte[] bytes);

	Optional<File> getFile(String hash);

	String saveFile(byte[] bytes) throws IOException;
}
