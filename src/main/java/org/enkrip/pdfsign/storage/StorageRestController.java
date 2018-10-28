package org.enkrip.pdfsign.storage;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf")
public class StorageRestController {
	private final StorageService fileService;

	public StorageRestController(StorageService fileService) {
		this.fileService = fileService;
	}

	private byte[] fileToByteArray(File file) {
		try {
			return FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@GetMapping(path = "/{hash}")
	public ResponseEntity<byte[]> getFile(@PathVariable("hash") String hash) {
		return fileService.getFile(hash)
				.map(this::fileToByteArray)
				.map(ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)::body)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping(consumes = MediaType.APPLICATION_PDF_VALUE)
	public HashRestData uploadFile(@RequestBody byte[] pdf) throws IOException {
		String hash = fileService.saveFile(pdf);
		return ImmutableHashRestData.builder().hash(hash).build();
	}
}
