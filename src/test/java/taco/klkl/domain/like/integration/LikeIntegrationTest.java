package taco.klkl.domain.like.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import taco.klkl.domain.like.domain.Like;
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
		// given

		// when & then
		mockMvc.perform(post("/v1/products/{id}/likes", product.getProductId()))
			.andExpect(status().isCreated());
		List<Like> all = likeRepository.findAll();
		assertThat(all).hasSize(1);
	}

	@Test
	@DisplayName("좋아요 DELETE 테스트")
	void testDeleteLike() throws Exception {
		// given

		// when & then
		mockMvc.perform(delete("/v1/products/{id}/likes", product.getProductId()))
			.andExpect(status().isNoContent());
		List<Like> all = likeRepository.findAll();
		assertThat(all).hasSize(0);
	}
}
