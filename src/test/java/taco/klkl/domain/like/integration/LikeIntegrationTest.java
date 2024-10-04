package taco.klkl.domain.like.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static taco.klkl.global.common.constants.TestConstants.TEST_UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import taco.klkl.domain.like.service.LikeService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.util.ResponseUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig.class)
@WithMockUser(username = TEST_UUID, roles = "USER")
public class LikeIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@Autowired
	private LikeService likeService;

	private final Long productId = 101L;

	@Test
	@DisplayName("좋아요 POST 테스트")
	void testPostLike() throws Exception {
		// when & then
		mockMvc.perform(post("/v1/likes/{productId}", productId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));
	}

	@Test
	@DisplayName("좋아요 DELETE 테스트")
	void testDeleteLike() throws Exception {
		// when & then
		mockMvc.perform(delete("/v1/likes/{productId}", productId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(0)));
	}

	@Test
	@DisplayName("여러번 좋아요 POST 테스트")
	void testPostLikeMultiple() throws Exception {
		// when & then
		mockMvc.perform(post("/v1/likes/{productId}", productId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

		mockMvc.perform(post("/v1/likes/{productId}", productId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));
	}

	@Test
	@DisplayName("여러번 좋아요 DELETE 테스트")
	void testDeleteLikeMultiple() throws Exception {
		// when & then
		mockMvc.perform(delete("/v1/likes/{productId}", productId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(0)));

		mockMvc.perform(delete("/v1/likes/{productId}", productId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(0)));
	}
}
