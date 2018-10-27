package org.enkrip.pdfsign.signature;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf/{hash}/signatures")
public class SignatureRestController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SignatureService signatureService;

	public SignatureRestController(SignatureService signatureService) {
		this.signatureService = signatureService;
	}

	@PutMapping
	public ResponseEntity<List<SignatureRestData>> getSignatures(@PathVariable String hash) {
		try {
			return ResponseEntity.ok(signatureService.getSignatures(hash));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResponseEntity.badRequest().build();
		}
	}
}
