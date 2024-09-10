package taco.klkl.domain.product.controller;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.request.ProductFilterOptions;
import taco.klkl.domain.product.dto.request.ProductSortOptions;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.common.response.PagedResponse;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private ProductSimpleResponse productSimpleResponse;
	private ProductDetailResponse productDetailResponse;
	private ProductCreateUpdateRequest productCreateUpdateRequest;

	@BeforeEach
	void setUp() {
		UserDetailResponse userDetailResponse = new UserDetailResponse(
			1L,
			"image/profileUrl.jpg",
			"userName",
			"userDescription",
			100
		);
		CityResponse cityResponse = new CityResponse(
			1L,
			"cityName"
		);
		SubcategoryResponse subcategoryResponse = new SubcategoryResponse(
			1L,
			"subcategoryName"
		);
		CurrencyResponse currencyResponse = new CurrencyResponse(
			1L,
			"currencyCode",
			"통화단위",
			"image/flagUrl.jpg"
		);
		TagResponse tagResponse1 = new TagResponse(
			1L,
			"tagName1"
		);
		TagResponse tagResponse2 = new TagResponse(
			2L,
			"tagName2"
		);

		productSimpleResponse = new ProductSimpleResponse(
			1L,
			"main-image.jpg",
			"productName",
			10,
			Rating.FIVE.getValue(),
			CountryType.THAILAND.getName(),
			CategoryType.FOOD.getName(),
			Set.of(tagResponse1, tagResponse2)
		);
		productDetailResponse = new ProductDetailResponse(
			1L,
			List.of("image1.jpg", "image2.jpg", "image3.jpg"),
			"productName",
			"Description",
			"123 Street",
			1000,
			10,
			Rating.FIVE.getValue(),
			userDetailResponse,
			cityResponse,
			subcategoryResponse,
			currencyResponse,
			Set.of(tagResponse1, tagResponse2),
			LocalDateTime.now()
		);
		productCreateUpdateRequest = new ProductCreateUpdateRequest(
			"productName",
			"productDescription",
			"productAddress",
			1000,
			Rating.FIVE.getValue(),
			1L,
			1L,
			1L,
			Set.of(1L, 2L)
		);
	}

	@Test
	@DisplayName("상품 목록 조회 - 성공")
	void testFindProducts_ShouldReturnPagedProductsByFilteringAndSorting() throws Exception {
		// Given
		List<ProductSimpleResponse> products = List.of(productSimpleResponse);
		PagedResponse<ProductSimpleResponse> pagedResponse = new PagedResponse<>(
			products, 0, 10, 1, 1, true
		);
		when(productService.findProductsByFilterOptionsAndSortOptions(
			any(Pageable.class),
			any(ProductFilterOptions.class),
			any(ProductSortOptions.class)))
			.thenReturn(pagedResponse);

		// When & Then
		mockMvc.perform(get("/v1/products")
				.param("page", "0")
				.param("size", "10")
				.param("city_id", "4", "5")
				.param("subcategory_id", "6", "7")
				.param("tag_id", "1", "2")
				.param("sort_by", "rating")
				.param("sort_direction", "DESC"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.content", hasSize(1)))
			.andExpect(jsonPath("$.data.content[0].id",
				is(productSimpleResponse.id().intValue())))
			.andExpect(jsonPath("$.data.content[0].name", is(productSimpleResponse.name())))
			.andExpect(jsonPath("$.data.content[0].likeCount", is(productSimpleResponse.likeCount())))
			.andExpect(jsonPath("$.data.content[0].rating", is(productSimpleResponse.rating())))
			.andExpect(jsonPath("$.data.content[0].countryName", is(productSimpleResponse.countryName())))
			.andExpect(jsonPath("$.data.content[0].categoryName",
				is(productSimpleResponse.categoryName())))
			.andExpect(jsonPath("$.data.content[0].tags",
				hasSize(productSimpleResponse.tags().size())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(10)))
			.andExpect(jsonPath("$.data.totalElements", is(1)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		// Verify that the service method was called with correct parameters
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<ProductFilterOptions> filterOptionsCaptor =
			ArgumentCaptor.forClass(ProductFilterOptions.class);
		ArgumentCaptor<ProductSortOptions> sortOptionsCaptor = ArgumentCaptor.forClass(ProductSortOptions.class);

		verify(productService).findProductsByFilterOptionsAndSortOptions(
			pageableCaptor.capture(),
			filterOptionsCaptor.capture(),
			sortOptionsCaptor.capture()
		);

		Pageable capturedPageable = pageableCaptor.getValue();
		ProductFilterOptions capturedFilterOptions = filterOptionsCaptor.getValue();
		ProductSortOptions capturedSortOptions = sortOptionsCaptor.getValue();

		assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
		assertThat(capturedPageable.getPageSize()).isEqualTo(10);
		assertThat(capturedFilterOptions.cityIds()).containsExactly(4L, 5L);
		assertThat(capturedFilterOptions.subcategoryIds()).containsExactly(6L, 7L);
		assertThat(capturedFilterOptions.tagIds()).containsExactly(1L, 2L);
		assertThat(capturedSortOptions.sortBy()).isEqualTo("rating");
		assertThat(capturedSortOptions.sortDirection()).isEqualTo("DESC");
	}

	@Test
	@DisplayName("제목으로 상품 목록 조회 - 성공")
	void testFindProductsByPartialNameAndSortOption() throws Exception {
		// Given
		List<ProductSimpleResponse> products = List.of(productSimpleResponse);
		PagedResponse<ProductSimpleResponse> pagedResponse = new PagedResponse<>(
			products, 0, 10, 1, 1, true
		);
		when(productService.findProductsByPartialName(
			any(String.class),
			any(Pageable.class),
			any(ProductSortOptions.class)))
			.thenReturn(pagedResponse);

		// When & Then
		mockMvc.perform(get("/v1/products/search")
				.param("page", "0")
				.param("size", "10")
				.param("name", "name")
				.param("sort_by", "rating")
				.param("sort_direction", "DESC"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.content", hasSize(1)))
			.andExpect(jsonPath("$.data.content[0].id",
				is(productSimpleResponse.id().intValue())))
			.andExpect(jsonPath("$.data.content[0].name", is(productSimpleResponse.name())))
			.andExpect(jsonPath("$.data.content[0].likeCount", is(productSimpleResponse.likeCount())))
			.andExpect(jsonPath("$.data.content[0].rating", is(productSimpleResponse.rating())))
			.andExpect(jsonPath("$.data.content[0].countryName", is(productSimpleResponse.countryName())))
			.andExpect(jsonPath("$.data.content[0].categoryName",
				is(productSimpleResponse.categoryName())))
			.andExpect(jsonPath("$.data.content[0].tags",
				hasSize(productSimpleResponse.tags().size())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(10)))
			.andExpect(jsonPath("$.data.totalElements", is(1)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		// Verify that the service method was called with correct parameters
		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		ArgumentCaptor<ProductSortOptions> sortOptionsCaptor = ArgumentCaptor.forClass(ProductSortOptions.class);

		verify(productService).findProductsByPartialName(any(String.class), pageableCaptor.capture(),
			sortOptionsCaptor.capture());

		Pageable capturedPageable = pageableCaptor.getValue();
		ProductSortOptions capturedSortOptions = sortOptionsCaptor.getValue();

		assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
		assertThat(capturedPageable.getPageSize()).isEqualTo(10);
		assertThat(capturedSortOptions.sortBy()).isEqualTo("rating");
		assertThat(capturedSortOptions.sortDirection()).isEqualTo("DESC");

	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void testFindProductById_ShouldReturnProduct() throws Exception {
		// Given
		when(productService.findProductById(1L)).thenReturn(productDetailResponse);

		// When & Then
		mockMvc.perform(get("/v1/products/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(productDetailResponse.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponse.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponse.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponse.address())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponse.price())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponse.likeCount())))
			.andExpect(jsonPath("$.data.rating", is(productSimpleResponse.rating())))
			.andExpect(jsonPath("$.data.user.id", is(productDetailResponse.user().id().intValue())))
			.andExpect(jsonPath("$.data.user.profileUrl",
				is(productDetailResponse.user().profileUrl())))
			.andExpect(jsonPath("$.data.user.name", is(productDetailResponse.user().name())))
			.andExpect(jsonPath("$.data.user.description",
				is(productDetailResponse.user().description())))
			.andExpect(jsonPath("$.data.user.totalLikeCount",
				is(productDetailResponse.user().totalLikeCount())))
			.andExpect(jsonPath("$.data.city.id",
				is(productDetailResponse.city().id().intValue())))
			.andExpect(jsonPath("$.data.city.name", is(productDetailResponse.city().name())))
			.andExpect(jsonPath("$.data.subcategory.id",
				is(productDetailResponse.subcategory().id().intValue())))
			.andExpect(jsonPath("$.data.subcategory.name",
				is(productDetailResponse.subcategory().name())))
			.andExpect(jsonPath("$.data.currency.id",
				is(productDetailResponse.currency().id().intValue())))
			.andExpect(jsonPath("$.data.currency.code", is(productDetailResponse.currency().code())))
			.andExpect(jsonPath("$.data.tags", hasSize(productSimpleResponse.tags().size())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 등록 - 성공")
	void testCreateProduct_ShouldReturnCreatedProduct() throws Exception {
		// Given
		when(productService.createProduct(any(ProductCreateUpdateRequest.class)))
			.thenReturn(productDetailResponse);

		// When & Then
		mockMvc.perform(post("/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreateUpdateRequest)))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", containsString("/v1/products/")))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(productDetailResponse.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponse.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponse.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponse.address())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponse.price())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponse.likeCount())))
			.andExpect(jsonPath("$.data.rating", is(productSimpleResponse.rating())))
			.andExpect(jsonPath("$.data.user.id", is(productDetailResponse.user().id().intValue())))
			.andExpect(jsonPath("$.data.user.profileUrl",
				is(productDetailResponse.user().profileUrl())))
			.andExpect(jsonPath("$.data.user.name", is(productDetailResponse.user().name())))
			.andExpect(jsonPath("$.data.user.description",
				is(productDetailResponse.user().description())))
			.andExpect(jsonPath("$.data.user.totalLikeCount",
				is(productDetailResponse.user().totalLikeCount())))
			.andExpect(jsonPath("$.data.city.id",
				is(productDetailResponse.city().id().intValue())))
			.andExpect(jsonPath("$.data.city.name", is(productDetailResponse.city().name())))
			.andExpect(jsonPath("$.data.subcategory.id",
				is(productDetailResponse.subcategory().id().intValue())))
			.andExpect(jsonPath("$.data.subcategory.name",
				is(productDetailResponse.subcategory().name())))
			.andExpect(jsonPath("$.data.currency.id",
				is(productDetailResponse.currency().id().intValue())))
			.andExpect(jsonPath("$.data.currency.code", is(productDetailResponse.currency().code())))
			.andExpect(jsonPath("$.data.tags", hasSize(productSimpleResponse.tags().size())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 정보 수정 - 성공")
	void testUpdateProduct_ShouldReturnUpdatedProduct() throws Exception {
		// Given
		when(productService.updateProduct(any(Long.class), any(ProductCreateUpdateRequest.class)))
			.thenReturn(productDetailResponse);

		// When & Then
		mockMvc.perform(put("/v1/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreateUpdateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(productDetailResponse.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponse.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponse.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponse.address())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponse.price())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponse.likeCount())))
			.andExpect(jsonPath("$.data.rating", is(productSimpleResponse.rating())))
			.andExpect(jsonPath("$.data.user.id", is(productDetailResponse.user().id().intValue())))
			.andExpect(jsonPath("$.data.user.profileUrl",
				is(productDetailResponse.user().profileUrl())))
			.andExpect(jsonPath("$.data.user.name", is(productDetailResponse.user().name())))
			.andExpect(jsonPath("$.data.user.description",
				is(productDetailResponse.user().description())))
			.andExpect(jsonPath("$.data.user.totalLikeCount",
				is(productDetailResponse.user().totalLikeCount())))
			.andExpect(jsonPath("$.data.city.id",
				is(productDetailResponse.city().id().intValue())))
			.andExpect(jsonPath("$.data.city.name", is(productDetailResponse.city().name())))
			.andExpect(jsonPath("$.data.subcategory.id",
				is(productDetailResponse.subcategory().id().intValue())))
			.andExpect(jsonPath("$.data.subcategory.name",
				is(productDetailResponse.subcategory().name())))
			.andExpect(jsonPath("$.data.currency.id",
				is(productDetailResponse.currency().id().intValue())))
			.andExpect(jsonPath("$.data.currency.code", is(productDetailResponse.currency().code())))
			.andExpect(jsonPath("$.data.tags", hasSize(productSimpleResponse.tags().size())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 삭제 - 성공")
	void testDeleteProduct_ShouldReturnNoContent() throws Exception {
		// Given
		doNothing().when(productService).deleteProduct(1L);

		// When & Then
		mockMvc.perform(delete("/v1/products/1"))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", nullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
