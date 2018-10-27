package org.enkrip.pdfsign.storage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface StorageService {
	String saveFile(byte[] bytes) throws IOException;

	Optional<File> getFile(String hash);
}
