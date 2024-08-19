package taco.klkl.global.config.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional(readOnly = true)
public class WebConfigTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("CORS헤더 확인 테스트")
	void testCorsHeaderResponse() throws Exception {
		mockMvc.perform(options("/v1/regions")
				.header("Origin", "http://localhost:3000")
				.header("Access-Control-Request-Method", "GET")
				.header("Access-Control-Request-Headers", "Authorization, Content-Type"))
			.andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
			.andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,HEAD,OPTIONS"))
			.andExpect(header().string("Access-Control-Allow-Headers", "Authorization, Content-Type"))
			.andExpect(header().string("Access-Control-Max-Age", "7200"));
	}

}
