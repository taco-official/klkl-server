package taco.klkl.domain.like.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.service.LikeService;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.global.util.UserUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LikeIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	LikeService likeService;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserUtil userUtil;

	Product product;

	@BeforeEach
	void setUp() {
		ProductCreateRequestDto createDto = new ProductCreateRequestDto(
			"name",
			"description",
			"address",
			1000,
			2L,
			3L,
			4L
		);
		ProductDetailResponseDto productResponseDto = productService.createProduct(createDto);
		product = productService.getProductById(productResponseDto.productId());
	}

	@AfterEach
	void afterEach() {
		likeService.deleteLike(product.getProductId());
	}

	@Test
	@DisplayName("좋아요 POST 테스트")
	void testPostLike() throws Exception {
		// when & then
		mockMvc.perform(post("/v1/products/{productId}/likes", product.getProductId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

	}

	@Test
	@DisplayName("좋아요 DELETE 테스트")
	void testDeleteLike() throws Exception {
		// when & then
		mockMvc.perform(delete("/v1/products/{productId}/likes", product.getProductId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(0)));

	}

	@Test
	@DisplayName("여러번 좋아요 POST 테스트")
	void testPostLikeMultiple() throws Exception {
		// when & then
		mockMvc.perform(post("/v1/products/{productId}/likes", product.getProductId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

		mockMvc.perform(post("/v1/products/{productId}/likes", product.getProductId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(true)))
			.andExpect(jsonPath("$.data.likeCount", is(1)));

	}

	@Test
	@DisplayName("여러번 좋아요 DELETE 테스트")
	void testDeleteLikeMultiple() throws Exception {
		// when & then
		mockMvc.perform(delete("/v1/products/{productId}/likes", product.getProductId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(0)));

		mockMvc.perform(delete("/v1/products/{productId}/likes", product.getProductId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.isLiked", is(false)))
			.andExpect(jsonPath("$.data.likeCount", is(0)));
	}
}
