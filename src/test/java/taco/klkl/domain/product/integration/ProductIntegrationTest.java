package taco.klkl.domain.product.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.global.common.constants.ProductConstants;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Product 통합 테스트")
public class ProductIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@Test
	@DisplayName("상품 상세 조회 API 테스트")
	public void testGetProductById() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest = new ProductCreateUpdateRequest(
			"name",
			"description",
			"address",
			1000,
			5.0,
			414L,
			310L,
			438L,
			Set.of(350L, 351L)
		);
		ProductDetailResponse productDto = productService.createProduct(createRequest);

		// when & then
		mockMvc.perform(get("/v1/products/" + productDto.id())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", notNullValue()))
			.andExpect(jsonPath("$.data.name", is(createRequest.name())))
			.andExpect(jsonPath("$.data.description", is(createRequest.description())))
			.andExpect(jsonPath("$.data.address", is(createRequest.address())))
			.andExpect(jsonPath("$.data.price", is(createRequest.price())))
			.andExpect(jsonPath("$.data.likeCount", is(ProductConstants.DEFAULT_LIKE_COUNT)))
			.andExpect(jsonPath("$.data.rating", is(createRequest.rating())))
			.andExpect(jsonPath("$.data.user.id", notNullValue()))
			.andExpect(jsonPath("$.data.city.cityId", is(createRequest.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.id", is(createRequest.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currency.currencyId", is(createRequest.currencyId().intValue())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 등록 API 테스트")
	public void testCreateProduct() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest = new ProductCreateUpdateRequest(
			"name",
			"description",
			"address",
			1000,
			5.0,
			414L,
			310L,
			438L,
			Set.of(350L, 351L)
		);

		// when & then
		mockMvc.perform(post("/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(createRequest)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", notNullValue()))
			.andExpect(jsonPath("$.data.name", is(createRequest.name())))
			.andExpect(jsonPath("$.data.description", is(createRequest.description())))
			.andExpect(jsonPath("$.data.address", is(createRequest.address())))
			.andExpect(jsonPath("$.data.price", is(createRequest.price())))
			.andExpect(jsonPath("$.data.likeCount", is(ProductConstants.DEFAULT_LIKE_COUNT)))
			.andExpect(jsonPath("$.data.rating", is(createRequest.rating())))
			.andExpect(jsonPath("$.data.user.id", notNullValue()))
			.andExpect(jsonPath("$.data.city.cityId", is(createRequest.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.id", is(createRequest.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currency.currencyId", is(createRequest.currencyId().intValue())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("페이지네이션으로 상품 목록 조회 API 테스트")
	public void testGetProductsByPagination() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			414L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequest createRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			415L,
			311L,
			438L,
			null
		);
		productService.createProduct(createRequest1);
		productService.createProduct(createRequest2);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(5)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRequest2.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRequest1.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(10)))
			.andExpect(jsonPath("$.data.totalElements", is(5)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("단일 도시 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsBySingleCityId() throws Exception {
		// given
		ProductCreateUpdateRequest createOsakaRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			414L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequest createOsakaRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			414L,
			311L,
			438L,
			null
		);
		ProductCreateUpdateRequest createTokyoRequest = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			2000,
			5.0,
			416L,
			311L,
			438L,
			null
		);
		productService.createProduct(createOsakaRequest1);
		productService.createProduct(createOsakaRequest2);
		productService.createProduct(createTokyoRequest);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("city_id", "416")  // 도쿄
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(1)))
			.andExpect(jsonPath("$.data.content[0].name", is(createTokyoRequest.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(1)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("여러 도시 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsByMultipleCityIds() throws Exception {
		// given
		ProductCreateUpdateRequest createKyotoRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequest createKyotoRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			415L,
			311L,
			438L,
			null
		);
		ProductCreateUpdateRequest createTokyoRequest = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			2000,
			5.0,
			416L,
			311L,
			438L,
			null
		);
		productService.createProduct(createKyotoRequest1);
		productService.createProduct(createKyotoRequest2);
		productService.createProduct(createTokyoRequest);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("city_id", "415", "416")  // 교토, 도쿄
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(3)))
			.andExpect(jsonPath("$.data.content[0].name", is(createKyotoRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createKyotoRequest2.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createTokyoRequest.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(3)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("단일 소분류 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsBySingleSubcategoryId() throws Exception {
		// given
		ProductCreateUpdateRequest createRamenRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequest createRamenRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			431L,
			310L,
			442L,
			null
		);
		ProductCreateUpdateRequest createShoeRequest = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			5.0,
			416L,
			324L,
			438L,
			null
		);
		ProductCreateUpdateRequest createAlcoholRequest = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			null
		);
		productService.createProduct(createRamenRequest1);
		productService.createProduct(createRamenRequest2);
		productService.createProduct(createShoeRequest);
		productService.createProduct(createAlcoholRequest);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("subcategory_id", "310")  // 라면
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(2)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRamenRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRamenRequest2.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(2)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("여러 소분류 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsByMultipleSubcategoryIds() throws Exception {
		// given
		ProductCreateUpdateRequest createRamenRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequest createRamenRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			431L,
			310L,
			442L,
			null
		);
		ProductCreateUpdateRequest createShoeRequest = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			5.0,
			416L,
			324L,
			438L,
			null
		);
		ProductCreateUpdateRequest createAlcoholRequest = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			null
		);
		productService.createProduct(createRamenRequest1);
		productService.createProduct(createRamenRequest2);
		productService.createProduct(createShoeRequest);
		productService.createProduct(createAlcoholRequest);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("subcategory_id", "310", "324")  // 라면, 신발
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(3)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRamenRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRamenRequest2.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createShoeRequest.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(3)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("단일 태그 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsBySingleTagId() throws Exception {
		// given
		ProductCreateUpdateRequest createCilantroRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createCilantroRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			431L,
			310L,
			442L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			5.0,
			416L,
			324L,
			438L,
			null
		);
		ProductCreateUpdateRequest createConvenienceStoreRequest = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			Set.of(350L)
		);
		productService.createProduct(createCilantroRequest1);
		productService.createProduct(createCilantroRequest2);
		productService.createProduct(createRequest);
		productService.createProduct(createConvenienceStoreRequest);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("tag_id", "351")  // 고수
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(2)))
			.andExpect(jsonPath("$.data.content[0].name", is(createCilantroRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createCilantroRequest2.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(2)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("다중 태그 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsByMultipleTagIds() throws Exception {
		// given
		ProductCreateUpdateRequest createCilantroRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createCilantroRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			431L,
			310L,
			442L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			5.0,
			416L,
			324L,
			438L,
			null
		);
		ProductCreateUpdateRequest createConvenienceStoreRequest = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			Set.of(350L)
		);
		productService.createProduct(createCilantroRequest1);
		productService.createProduct(createCilantroRequest2);
		productService.createProduct(createRequest);
		productService.createProduct(createConvenienceStoreRequest);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("tag_id", "351", "350")  // 고수, 편의점
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(3)))
			.andExpect(jsonPath("$.data.content[0].name", is(createCilantroRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createCilantroRequest2.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createConvenienceStoreRequest.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(3)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("생성된 날짜로 오름차순 정렬된 상품 목록 조회 API 테스트")
	public void testSortProductsByCreatedAtAsc() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			431L,
			310L,
			442L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest3 = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			5.0,
			416L,
			324L,
			438L,
			Set.of(350L, 351L)
		);
		ProductCreateUpdateRequest createRequest4 = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			Set.of(350L)
		);
		productService.createProduct(createRequest1);
		productService.createProduct(createRequest2);
		productService.createProduct(createRequest3);
		productService.createProduct(createRequest4);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("tag_id", "351", "350")
				.param("sort_by", "createdAt")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(4)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRequest2.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createRequest3.name())))
			.andExpect(jsonPath("$.data.content[3].name", is(createRequest4.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(4)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("생성된 날짜로 내림차순 정렬된 상품 목록 조회 API 테스트")
	public void testSortProductsByCreatedAtDesc() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			5.0,
			415L,
			310L,
			438L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			5.0,
			431L,
			310L,
			442L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest3 = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			5.0,
			416L,
			324L,
			438L,
			Set.of(350L, 351L)
		);
		ProductCreateUpdateRequest createRequest4 = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			Set.of(350L)
		);
		productService.createProduct(createRequest4);
		productService.createProduct(createRequest3);
		productService.createProduct(createRequest2);
		productService.createProduct(createRequest1);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("tag_id", "351", "350")
				.param("sort_by", "createdAt")
				.param("sort_direction", "DESC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(4)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRequest2.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createRequest3.name())))
			.andExpect(jsonPath("$.data.content[3].name", is(createRequest4.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(4)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("평점으로 오름차순 정렬된 상품 목록 조회 API 테스트")
	public void testSortProductsByRatingAsc() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			0.5,
			415L,
			310L,
			438L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			2.0,
			431L,
			310L,
			442L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest3 = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			3.5,
			416L,
			324L,
			438L,
			Set.of(350L, 351L)
		);
		ProductCreateUpdateRequest createRequest4 = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			Set.of(350L)
		);
		productService.createProduct(createRequest1);
		productService.createProduct(createRequest2);
		productService.createProduct(createRequest3);
		productService.createProduct(createRequest4);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("tag_id", "351", "350")
				.param("sort_by", "rating")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(4)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRequest2.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createRequest3.name())))
			.andExpect(jsonPath("$.data.content[3].name", is(createRequest4.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(4)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("평점으로 내림차순 정렬된 상품 목록 조회 API 테스트")
	public void testSortProductsByRatingDesc() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest1 = new ProductCreateUpdateRequest(
			"name1",
			"description1",
			"address1",
			1000,
			0.5,
			415L,
			310L,
			438L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest2 = new ProductCreateUpdateRequest(
			"name2",
			"description2",
			"address2",
			2000,
			2.0,
			431L,
			310L,
			442L,
			Set.of(351L)
		);
		ProductCreateUpdateRequest createRequest3 = new ProductCreateUpdateRequest(
			"name3",
			"description3",
			"address3",
			3000,
			3.5,
			416L,
			324L,
			438L,
			Set.of(350L, 351L)
		);
		ProductCreateUpdateRequest createRequest4 = new ProductCreateUpdateRequest(
			"name4",
			"description4",
			"address4",
			4000,
			5.0,
			423L,
			315L,
			439L,
			Set.of(350L)
		);
		productService.createProduct(createRequest1);
		productService.createProduct(createRequest2);
		productService.createProduct(createRequest3);
		productService.createProduct(createRequest4);

		// when & then
		mockMvc.perform(get("/v1/products")
				.param("tag_id", "351", "350")
				.param("sort_by", "rating")
				.param("sort_direction", "DESC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(4)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRequest4.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRequest3.name())))
			.andExpect(jsonPath("$.data.content[2].name", is(createRequest2.name())))
			.andExpect(jsonPath("$.data.content[3].name", is(createRequest1.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(4)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("좋아요 수로 오름차순 정렬된 상품 목록 조회 API 테스트")
	public void testSortProductsByLikeCountAsc() throws Exception {
		// when & then
		mockMvc.perform(get("/v1/products")
				.param("sort_by", "likeCount")
				.param("sort_direction", "ASC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(3)))
			.andExpect(jsonPath("$.data.content[0].name", is("왕족발 보쌈 과자")))
			.andExpect(jsonPath("$.data.content[1].name", is("곤약젤리")))
			.andExpect(jsonPath("$.data.content[2].name", is("여름 원피스")))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(3)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("좋아요 수로 내림차순 정렬된 상품 목록 조회 API 테스트")
	public void testSortProductsByLikeCountDesc() throws Exception {
		// when & then
		mockMvc.perform(get("/v1/products")
				.param("sort_by", "likeCount")
				.param("sort_direction", "DESC")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(3)))
			.andExpect(jsonPath("$.data.content[0].name", is("여름 원피스")))
			.andExpect(jsonPath("$.data.content[1].name", is("곤약젤리")))
			.andExpect(jsonPath("$.data.content[2].name", is("왕족발 보쌈 과자")))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(ProductConstants.DEFAULT_PAGE_SIZE)))
			.andExpect(jsonPath("$.data.totalElements", is(3)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 수정 API 테스트")
	public void testUpdateProduct() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest = new ProductCreateUpdateRequest(
			"name",
			"description",
			"address",
			1000,
			5.0,
			414L,
			310L,
			438L,
			null
		);
		ProductDetailResponse productDto = productService.createProduct(createRequest);

		ProductCreateUpdateRequest updateRequest = new ProductCreateUpdateRequest(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			2000,
			4.5,
			415L,
			310L,
			438L,
			Set.of(350L, 351L)
		);

		// when & then
		mockMvc.perform(put("/v1/products/" + productDto.id())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(updateRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(productDto.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(updateRequest.name())))
			.andExpect(jsonPath("$.data.description", is(updateRequest.description())))
			.andExpect(jsonPath("$.data.address", is(updateRequest.address())))
			.andExpect(jsonPath("$.data.price", is(updateRequest.price())))
			.andExpect(jsonPath("$.data.likeCount", is(ProductConstants.DEFAULT_LIKE_COUNT)))
			.andExpect(jsonPath("$.data.rating", is(updateRequest.rating())))
			.andExpect(jsonPath("$.data.user.id", is(productDto.user().id().intValue())))
			.andExpect(jsonPath("$.data.city.cityId", is(updateRequest.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.id",
				is(updateRequest.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currency.currencyId", is(updateRequest.currencyId().intValue())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 삭제 API 테스트")
	public void testDeleteProduct() throws Exception {
		// given
		ProductCreateUpdateRequest createRequest = new ProductCreateUpdateRequest(
			"name",
			"description",
			"address",
			1000,
			5.0,
			414L,
			310L,
			438L,
			Set.of(350L, 351L)
		);
		ProductDetailResponse productDto = productService.createProduct(createRequest);

		// when & then
		mockMvc.perform(delete("/v1/products/" + productDto.id())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", nullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
