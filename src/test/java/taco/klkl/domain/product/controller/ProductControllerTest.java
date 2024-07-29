package taco.klkl.domain.product.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.domain.User;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ProductService productService;

	@Autowired
	ObjectMapper objectMapper;

	private ProductDetailResponseDto productDetailResponseDto;
	private ProductCreateRequestDto productCreateRequestDto;

	@BeforeEach
	public void setUp() {
		// Mock User 객체 생성
		User mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);

		// Mock Product 객체 생성
		Product mockProduct = mock(Product.class);
		when(mockProduct.getProductId()).thenReturn(1L);
		when(mockProduct.getUser()).thenReturn(mockUser);
		when(mockProduct.getName()).thenReturn("name");
		when(mockProduct.getDescription()).thenReturn("description");
		when(mockProduct.getAddress()).thenReturn("address");
		when(mockProduct.getLikeCount()).thenReturn(0);
		when(mockProduct.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(mockProduct.getPrice()).thenReturn(1000);
		when(mockProduct.getCityId()).thenReturn(2L);
		when(mockProduct.getSubcategoryId()).thenReturn(3L);
		when(mockProduct.getCurrencyId()).thenReturn(4L);

		// ProductDetailResponseDto 생성
		productDetailResponseDto = ProductDetailResponseDto.from(mockProduct);

		// ProductCreateRequestDto 생성
		productCreateRequestDto = new ProductCreateRequestDto(
			"name",
			"description",
			2L,
			3L,
			4L,
			"address",
			1000
		);
	}

	@Test
	@DisplayName("상품 상세 조회 API 테스트")
	public void testGetProductInfoById() throws Exception {
		// given
		when(productService.getProductInfoById(1L)).thenReturn(productDetailResponseDto);

		// when & then
		mockMvc.perform(get("/v1/products/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.productId", is(productDetailResponseDto.productId().intValue())))
			.andExpect(jsonPath("$.data.userId", is(productDetailResponseDto.userId().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponseDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponseDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponseDto.address())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponseDto.likeCount())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponseDto.price())))
			.andExpect(jsonPath("$.data.cityId", is(productDetailResponseDto.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategoryId", is(productDetailResponseDto.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currencyId", is(productDetailResponseDto.currencyId().intValue())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 등록 API 테스트")
	public void testCreateProduct() throws Exception {
		// given
		when(productService.createProduct(any(ProductCreateRequestDto.class))).thenReturn(productDetailResponseDto);

		// when & then
		mockMvc.perform(post("/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreateRequestDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.productId", is(productDetailResponseDto.productId().intValue())))
			.andExpect(jsonPath("$.data.userId", is(productDetailResponseDto.userId().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponseDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponseDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponseDto.address())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponseDto.likeCount())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponseDto.price())))
			.andExpect(jsonPath("$.data.cityId", is(productDetailResponseDto.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategoryId", is(productDetailResponseDto.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currencyId", is(productDetailResponseDto.currencyId().intValue())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
