package org.enkrip.pdfsign.hash;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HashRestController.class)
public class HashRestControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@SpyBean
	private HashServiceImpl hashService;

	@Test
	public void testHash() throws Exception {
		try (InputStream pdf = ClassLoader.getSystemResourceAsStream("sample.pdf")) {
			MockHttpServletResponse response = mockMvc.perform(put("/api/hash").content(IOUtils.toByteArray(pdf)).contentType(MediaType.APPLICATION_PDF))
					.andReturn()
					.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
			assertThat(response.getContentType()).startsWith(MediaType.TEXT_PLAIN_VALUE);
			assertThat(response.getContentAsString()).isEqualTo("8decc8571946d4cd70a024949e033a2a2a54377fe9f1c1b944c20f9ee11a9e51");
		}
	}
}