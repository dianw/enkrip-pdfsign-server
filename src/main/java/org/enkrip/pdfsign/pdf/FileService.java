package org.enkrip.pdfsign.pdf;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface FileService {
	String saveFile(byte[] bytes) throws IOException;

	Optional<File> getFile(String hash);
}
