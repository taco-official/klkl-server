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
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
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
		ProductCreateUpdateRequestDto createRequest = new ProductCreateUpdateRequestDto(
			"name",
			"description",
			"address",
			1000,
			414L,
			310L,
			438L,
			Set.of(350L, 351L)
		);
		ProductDetailResponseDto productDto = productService.createProduct(createRequest);

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
			.andExpect(jsonPath("$.data.user.id", notNullValue()))
			.andExpect(jsonPath("$.data.city.cityId", is(createRequest.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.subcategoryId", is(createRequest.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currency.currencyId", is(createRequest.currencyId().intValue())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
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
			.andExpect(jsonPath("$.data.user.id", notNullValue()))
			.andExpect(jsonPath("$.data.city.cityId", is(createRequest.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.subcategoryId", is(createRequest.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currency.currencyId", is(createRequest.currencyId().intValue())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("페이지네이션으로 상품 목록 조회 API 테스트")
	public void testGetProductsByPagination() throws Exception {
		// given
		ProductCreateUpdateRequestDto createRequest1 = new ProductCreateUpdateRequestDto(
			"name1",
			"description1",
			"address1",
			1000,
			414L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createRequest2 = new ProductCreateUpdateRequestDto(
			"name2",
			"description2",
			"address2",
			2000,
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
			.andExpect(jsonPath("$.data.content", hasSize(4)))
			.andExpect(jsonPath("$.data.content[0].name", is(createRequest1.name())))
			.andExpect(jsonPath("$.data.content[1].name", is(createRequest2.name())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(10)))
			.andExpect(jsonPath("$.data.totalElements", is(4)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("단일 도시 ID로 필터링된 상품 목록 조회 API 테스트")
	public void testGetProductsBySingleCityId() throws Exception {
		// given
		ProductCreateUpdateRequestDto createOsakaRequest1 = new ProductCreateUpdateRequestDto(
			"name1",
			"description1",
			"address1",
			1000,
			414L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createOsakaRequest2 = new ProductCreateUpdateRequestDto(
			"name2",
			"description2",
			"address2",
			2000,
			414L,
			311L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createTokyoRequest = new ProductCreateUpdateRequestDto(
			"name3",
			"description3",
			"address3",
			2000,
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
		ProductCreateUpdateRequestDto createKyotoRequest1 = new ProductCreateUpdateRequestDto(
			"name1",
			"description1",
			"address1",
			1000,
			415L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createKyotoRequest2 = new ProductCreateUpdateRequestDto(
			"name2",
			"description2",
			"address2",
			2000,
			415L,
			311L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createTokyoRequest = new ProductCreateUpdateRequestDto(
			"name3",
			"description3",
			"address3",
			2000,
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
	public void testGetProductsBySingleSubcategoryIds() throws Exception {
		// given
		ProductCreateUpdateRequestDto createRamenRequest1 = new ProductCreateUpdateRequestDto(
			"name1",
			"description1",
			"address1",
			1000,
			415L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createRamenRequest2 = new ProductCreateUpdateRequestDto(
			"name2",
			"description2",
			"address2",
			2000,
			431L,
			310L,
			442L,
			null
		);
		ProductCreateUpdateRequestDto createShoeRequest = new ProductCreateUpdateRequestDto(
			"name3",
			"description3",
			"address3",
			3000,
			416L,
			324L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createAlcoholRequest = new ProductCreateUpdateRequestDto(
			"name4",
			"description4",
			"address4",
			4000,
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
		ProductCreateUpdateRequestDto createRamenRequest1 = new ProductCreateUpdateRequestDto(
			"name1",
			"description1",
			"address1",
			1000,
			415L,
			310L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createRamenRequest2 = new ProductCreateUpdateRequestDto(
			"name2",
			"description2",
			"address2",
			2000,
			431L,
			310L,
			442L,
			null
		);
		ProductCreateUpdateRequestDto createShoeRequest = new ProductCreateUpdateRequestDto(
			"name3",
			"description3",
			"address3",
			3000,
			416L,
			324L,
			438L,
			null
		);
		ProductCreateUpdateRequestDto createAlcoholRequest = new ProductCreateUpdateRequestDto(
			"name4",
			"description4",
			"address4",
			4000,
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
	@DisplayName("상품 수정 API 테스트")
	public void testUpdateProduct() throws Exception {
		// given
		ProductCreateUpdateRequestDto createRequest = new ProductCreateUpdateRequestDto(
			"name",
			"description",
			"address",
			1000,
			414L,
			310L,
			438L,
			null
		);
		ProductDetailResponseDto productDto = productService.createProduct(createRequest);

		ProductCreateUpdateRequestDto updateRequest = new ProductCreateUpdateRequestDto(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			2000,
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
			.andExpect(jsonPath("$.data.user.id", is(productDto.user().id().intValue())))
			.andExpect(jsonPath("$.data.city.cityId", is(updateRequest.cityId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.subcategoryId",
				is(updateRequest.subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.currency.currencyId", is(updateRequest.currencyId().intValue())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 삭제 API 테스트")
	public void testDeleteProduct() throws Exception {
		// given
		ProductCreateUpdateRequestDto createRequest = new ProductCreateUpdateRequestDto(
			"name",
			"description",
			"address",
			1000,
			414L,
			310L,
			438L,
			Set.of(350L, 351L)
		);
		ProductDetailResponseDto productDto = productService.createProduct(createRequest);

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
