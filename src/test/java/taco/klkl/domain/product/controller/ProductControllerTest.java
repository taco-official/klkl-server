package taco.klkl.domain.product.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	ObjectMapper objectMapper;

	private ProductDetailResponseDto productDetailDto;

	private ProductSimpleResponseDto productSimpleDto1;
	private ProductSimpleResponseDto productSimpleDto2;

	@BeforeEach
	public void setUp() {
		// Mock 객체 생성
		User mockUser = mock(User.class);
		City mockCity = mock(City.class);
		Subcategory mockSubcategory = mock(Subcategory.class);
		Currency mockCurrency = mock(Currency.class);

		// Mock Product 객체 생성
		Product mockProduct1 = mock(Product.class);
		when(mockProduct1.getProductId()).thenReturn(1L);
		when(mockProduct1.getUser()).thenReturn(mockUser);
		when(mockProduct1.getName()).thenReturn("name1");
		when(mockProduct1.getDescription()).thenReturn("description1");
		when(mockProduct1.getAddress()).thenReturn("address1");
		when(mockProduct1.getLikeCount()).thenReturn(0);
		when(mockProduct1.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(mockProduct1.getPrice()).thenReturn(1000);
		when(mockProduct1.getCity()).thenReturn(mockCity);
		when(mockProduct1.getSubcategory()).thenReturn(mockSubcategory);
		when(mockProduct1.getCurrency()).thenReturn(mockCurrency);

		Product mockProduct2 = mock(Product.class);
		when(mockProduct2.getProductId()).thenReturn(2L);
		when(mockProduct2.getUser()).thenReturn(mockUser);
		when(mockProduct2.getName()).thenReturn("name2");
		when(mockProduct2.getDescription()).thenReturn("description2");
		when(mockProduct2.getAddress()).thenReturn("address2");
		when(mockProduct2.getLikeCount()).thenReturn(0);
		when(mockProduct2.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(mockProduct2.getPrice()).thenReturn(2000);
		when(mockProduct2.getCity()).thenReturn(mockCity);
		when(mockProduct2.getSubcategory()).thenReturn(mockSubcategory);
		when(mockProduct2.getCurrency()).thenReturn(mockCurrency);

		// ProductResponseDto 생성
		productDetailDto = ProductDetailResponseDto.from(mockProduct1);
		productSimpleDto1 = ProductSimpleResponseDto.from(mockProduct1);
		productSimpleDto2 = ProductSimpleResponseDto.from(mockProduct2);
	}

	@Test
	@DisplayName("기본 페이징 값으로 상품 목록 API 조회 테스트")
	void testGetAllProductsWithDefaultPaging() throws Exception {
		// Given
		List<ProductSimpleResponseDto> productDtos = Arrays.asList(productSimpleDto1, productSimpleDto2);

		when(productService.getAllProducts(any(Pageable.class))).thenReturn(productDtos);

		// When & Then
		mockMvc.perform(get("/v1/products")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].productId").value(productSimpleDto1.productId()))
			.andExpect(jsonPath("$.data[1].productId").value(productSimpleDto2.productId()));

		verify(productService).getAllProducts(argThat(pageable ->
			pageable.getPageNumber() == Integer.parseInt(ProductConstants.DEFAULT_PAGE_NUMBER)
				&& pageable.getPageSize() == Integer.parseInt(ProductConstants.DEFAULT_PAGE_SIZE)
		));
	}

	@Test
	@DisplayName("사용자 지정 페이징 값으로 상품 목록 API 조회 테스트")
	void testGetAllProductsWithCustomPaging() throws Exception {
		// Given
		List<ProductSimpleResponseDto> productDtos = Arrays.asList(productSimpleDto2, productSimpleDto2);

		when(productService.getAllProducts(any(Pageable.class))).thenReturn(productDtos);

		// When & Then
		mockMvc.perform(get("/v1/products")
				.param("page", "1")
				.param("size", "5")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].productId").value(productSimpleDto2.productId()))
			.andExpect(jsonPath("$.data[1].productId").value(productSimpleDto2.productId()));

		verify(productService).getAllProducts(argThat(pageable ->
			pageable.getPageNumber() == 1
				&& pageable.getPageSize() == 5
		));
	}

	@Test
	@DisplayName("상품 상세 조회 API 테스트")
	void testGetProductById() throws Exception {
		// Given
		when(productService.getProductById(1L)).thenReturn(productDetailDto);

		// When & Then
		mockMvc.perform(get("/v1/products/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.productId", is(productDetailDto.productId().intValue())))
			.andExpect(jsonPath("$.data.userId", is(productDetailDto.userId().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailDto.address())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailDto.likeCount())))
			.andExpect(jsonPath("$.data.price", is(productDetailDto.price())))
			.andExpect(jsonPath("$.data.cityId", is(productDetailDto.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategoryId", is(productDetailDto.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currencyId", is(productDetailDto.currencyId().intValue())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(productService).getProductById(1L);
	}

	@Test
	@DisplayName("상품 등록 API 테스트")
	public void testCreateProduct() throws Exception {
		// given
		ProductCreateUpdateRequestDto createRequest = new ProductCreateUpdateRequestDto(
			"name",
			"description",
			"address",
			1000,
			2L,
			3L,
			4L
		);
		when(productService.createProduct(any(ProductCreateUpdateRequestDto.class))).thenReturn(productDetailDto);

		// when & then
		mockMvc.perform(post("/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.productId", is(productDetailDto.productId().intValue())))
			.andExpect(jsonPath("$.data.userId", is(productDetailDto.userId().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailDto.address())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailDto.likeCount())))
			.andExpect(jsonPath("$.data.price", is(productDetailDto.price())))
			.andExpect(jsonPath("$.data.cityId", is(productDetailDto.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategoryId", is(productDetailDto.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currencyId", is(productDetailDto.currencyId().intValue())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(productService).createProduct(any(ProductCreateUpdateRequestDto.class));
	}

	@Test
	@DisplayName("상품 정보 수정 API 테스트")
	public void testUpdateProduct() throws Exception {
		// given
		Long productId = 1L;
		ProductCreateUpdateRequestDto updateRequest = new ProductCreateUpdateRequestDto(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			2000,
			2L,
			3L,
			4L
		);

		when(productService.updateProduct(eq(productId), any(ProductCreateUpdateRequestDto.class)))
			.thenReturn(productDetailDto);

		// when & then
		mockMvc.perform(put("/v1/products/{id}", productId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.productId", is(productDetailDto.productId().intValue())))
			.andExpect(jsonPath("$.data.userId", is(productDetailDto.userId().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailDto.address())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailDto.likeCount())))
			.andExpect(jsonPath("$.data.price", is(productDetailDto.price())))
			.andExpect(jsonPath("$.data.cityId", is(productDetailDto.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategoryId", is(productDetailDto.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currencyId", is(productDetailDto.currencyId().intValue())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(productService).updateProduct(eq(productId), any(ProductCreateUpdateRequestDto.class));
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
