package org.enkrip.pdfsign.hash;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hash")
public class HashRestController {
	private final HashService hashService;

	public HashRestController(HashService hashService) {
		this.hashService = hashService;
	}

	@PutMapping(consumes = MediaType.APPLICATION_PDF_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String hash(@RequestBody byte[] bytes) {
		return hashService.hash(bytes);
	}
}
