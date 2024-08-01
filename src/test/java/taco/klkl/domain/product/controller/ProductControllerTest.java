package taco.klkl.domain.product.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ProductService productService;

	@Autowired
	ObjectMapper objectMapper;

	private ProductDetailResponseDto productDetailResponseDto;

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
	}

	@Test
	@DisplayName("상품 상세 조회 API 테스트")
	public void testGetProductById() throws Exception {
		// given
		when(productService.getProductById(1L)).thenReturn(productDetailResponseDto);

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
		ProductCreateRequestDto productCreateRequestDto = new ProductCreateRequestDto(
			"name",
			"description",
			"address",
			1000,
			2L,
			3L,
			4L
		);
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

	@Test
	@DisplayName("상품 등록 API 유효성 검사 실패 테스트")
	public void testCreateProductValidationFailure() throws Exception {
		// given
		ProductCreateRequestDto invalidRequestDto = new ProductCreateRequestDto(
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);

		// when & then
		ErrorCode methodArgumentInvalidError = ErrorCode.METHOD_ARGUMENT_INVALID;
		mockMvc.perform(post("/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidRequestDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(methodArgumentInvalidError.getCode())))
			.andExpect(jsonPath("$.data.code", is(methodArgumentInvalidError.getCode())))
			.andExpect(jsonPath("$.data.message", containsString(methodArgumentInvalidError.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 정보 부분 업데이트 API 테스트")
	public void testUpdateProduct() throws Exception {
		// given
		Long productId = 1L;
		ProductUpdateRequestDto updateRequest = new ProductUpdateRequestDto(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			2000,
			2L,
			3L,
			4L
		);

		when(productService.updateProduct(eq(productId), any(ProductUpdateRequestDto.class)))
			.thenReturn(productDetailResponseDto);

		// when & then
		mockMvc.perform(patch("/v1/products/{id}", productId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
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
	@DisplayName("상품 삭제 API 테스트")
	public void testDeleteProduct() throws Exception {
		// given
		Long productId = 1L;
		doNothing().when(productService).deleteProduct(productId);

		// when & then
		mockMvc.perform(delete("/v1/products/{id}", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data").doesNotExist())
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(productService, times(1)).deleteProduct(productId);
	}

	@Test
	@DisplayName("존재하지 않는 상품 삭제 시 예외 처리 테스트")
	public void testDeleteNonExistentProduct() throws Exception {
		// given
		Long nonExistentProductId = 999L;
		doThrow(new ProductNotFoundException())
			.when(productService).deleteProduct(nonExistentProductId);

		// when & then
		ErrorCode productNotFoundError = ErrorCode.PRODUCT_NOT_FOUND;
		mockMvc.perform(delete("/v1/products/{id}", nonExistentProductId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(productNotFoundError.getCode())))
			.andExpect(jsonPath("$.data.code", is(productNotFoundError.getCode())))
			.andExpect(jsonPath("$.data.message", is(productNotFoundError.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(productService, times(1)).deleteProduct(nonExistentProductId);
	}
}
