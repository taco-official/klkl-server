package taco.klkl.domain.like.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.like.service.LikeService;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LikeService likeService;

	@InjectMocks
	private LikeController likeController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("좋아요 POST 테스트")
	void testAddLike() throws Exception {
		// given
		Long productId = 1L;

		// when & then
		mockMvc.perform(post("/v1/products/{id}/likes", productId))
			.andExpect(status().isCreated());

		verify(likeService).createLike(productId);
	}

	@Test
	@DisplayName("좋아요 DELETE 테스트")
	void testDeleteLike() throws Exception {
		// given
		Long productId = 1L;

		// when & then
		mockMvc.perform(delete("/v1/products/{id}/likes", productId))
			.andExpect(status().isNoContent());

		verify(likeService).deleteLike(productId);
	}
}
