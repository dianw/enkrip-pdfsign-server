package org.enkrip.pdfsign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PdfSignServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(PdfSignServerApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "pdf-sign")
	public PDFSignProperties pdfSignProperties() {
		return new PDFSignProperties();
	}

	public static class PDFSignProperties {
		private String defaultHashAlg = "SHA-256";
		private String storageLocation = "file:./target/pdf";

		public String getDefaultHashAlg() {
			return defaultHashAlg;
		}

		public void setDefaultHashAlg(String defaultHashAlg) {
			this.defaultHashAlg = defaultHashAlg;
		}

		public String getStorageLocation() {
			return storageLocation;
		}

		public void setStorageLocation(String storageLocation) {
			this.storageLocation = storageLocation;
		}
	}
}
