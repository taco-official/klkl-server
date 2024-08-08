package taco.klkl.domain.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.QProduct;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.request.ProductFilterOptionsDto;
import taco.klkl.domain.product.dto.response.PagedResponseDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.enums.CurrencyType;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CurrencyService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.UserConstants;
import taco.klkl.global.util.UserUtil;

class ProductServiceTest {

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CityService cityService;

	@Mock
	private CurrencyService currencyService;

	@Mock
	private SubcategoryService subcategoryService;

	@Mock
	private UserUtil userUtil;

	@InjectMocks
	private ProductService productService;

	private Product testProduct;
	private User user;
	private City city;
	private Subcategory subcategory;
	private Currency currency;
	private ProductCreateUpdateRequestDto requestDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = UserConstants.TEST_USER;

		Region region = Region.of(RegionType.SOUTHEAST_ASIA);
		currency = Currency.of(
			CurrencyType.THAI_BAHT,
			"image/baht.jpg"
		);
		Country country = Country.of(
			CountryType.JAPAN,
			region,
			"image/thailand-flag.jpg",
			"image/thailand-photo.jpg",
			currency
		);
		city = City.of(
			country,
			CityType.BANGKOK
		);

		Category category = Category.of(CategoryName.FOOD);
		subcategory = Subcategory.of(
			category,
			SubcategoryName.INSTANT_FOOD
		);

		testProduct = Product.of(
			"name",
			"description",
			"address",
			1000,
			user,
			city,
			subcategory,
			currency
		);

		requestDto = new ProductCreateUpdateRequestDto(
			"productName",
			"productDescription",
			"productAddress",
			1000,
			1L,
			1L,
			1L
		);
	}

	@Test
	@DisplayName("상품 목록 조회 - 성공")
	void testGetProductsByFilterOptions() {
		// Given
		List<Long> countryIds = List.of(1L, 2L, 3L);
		List<Long> cityIds = List.of(4L, 5L);
		ProductFilterOptionsDto filterOptions = new ProductFilterOptionsDto(
			countryIds,
			cityIds
		);
		Pageable pageable = PageRequest.of(0, 10);

		// Mocking QueryDSL behavior
		List<Product> productList = Arrays.asList(testProduct);

		JPAQuery<Product> productQuery = mock(JPAQuery.class);
		JPAQuery<Long> countQuery = mock(JPAQuery.class);

		QProduct product = QProduct.product;

		when(queryFactory.selectFrom(product)).thenReturn(productQuery);
		when(productQuery.where(any(BooleanBuilder.class))).thenReturn(productQuery);
		when(productQuery.offset(pageable.getOffset())).thenReturn(productQuery);
		when(productQuery.limit(pageable.getPageSize())).thenReturn(productQuery);
		when(productQuery.fetch()).thenReturn(productList);

		when(queryFactory.select(product.count())).thenReturn(countQuery);
		when(countQuery.from(product)).thenReturn(countQuery);
		when(countQuery.where(any(BooleanBuilder.class))).thenReturn(countQuery);
		when(countQuery.fetchOne()).thenReturn((long)productList.size());

		// When
		PagedResponseDto<ProductSimpleResponseDto> result = productService
			.getProductsByFilterOptions(pageable, filterOptions);

		// Then
		assertThat(result.content()).hasSize(1);
		assertThat(result.content().get(0).productId()).isEqualTo(testProduct.getId());
		assertThat(result.totalElements()).isEqualTo(1);
		assertThat(result.totalPages()).isEqualTo(1);
		assertThat(result.pageNumber()).isEqualTo(0);
		assertThat(result.pageSize()).isEqualTo(10);
		assertThat(result.last()).isTrue();

		// Verify that the query methods were called
		verify(queryFactory).selectFrom(product);
		verify(queryFactory).select(product.count());
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void testGetProductById() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

		// When
		ProductDetailResponseDto result = productService.getProductById(1L);

		// Then
		assertThat(result.productId()).isEqualTo(testProduct.getId());
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("상품 상세 조회 - 실패 (상품 없음)")
	void testGetProductById_NotFound() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("상품 생성 - 성공")
	void testCreateProduct() {
		// Given
		when(userUtil.findTestUser()).thenReturn(user);
		when(cityService.getCityById(1L)).thenReturn(city);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(subcategory);
		when(currencyService.getCurrencyById(1L)).thenReturn(currency);

		// save 메서드 호출 시 ID를 설정하고 저장된 객체를 반환하도록 설정
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			// ID 설정 (실제로는 데이터베이스에서 생성됨)
			ReflectionTestUtils.setField(savedProduct, "id", 1L);
			return savedProduct;
		});

		// When
		ProductDetailResponseDto result = productService.createProduct(requestDto);

		// Then
		assertThat(result.productId()).isEqualTo(1L);
		verify(productRepository).save(argThat(savedProduct -> {
			assertThat(savedProduct.getId()).isEqualTo(1L);
			assertThat(savedProduct.getName()).isEqualTo(requestDto.name());
			assertThat(savedProduct.getDescription()).isEqualTo(requestDto.description());
			assertThat(savedProduct.getAddress()).isEqualTo(requestDto.address());
			assertThat(savedProduct.getPrice()).isEqualTo(requestDto.price());
			assertThat(savedProduct.getUser()).isEqualTo(user);
			assertThat(savedProduct.getCity()).isEqualTo(city);
			assertThat(savedProduct.getSubcategory()).isEqualTo(subcategory);
			assertThat(savedProduct.getCurrency()).isEqualTo(currency);
			return true;
		}));
	}

	@Test
	@DisplayName("상품 수정 - 성공")
	void testUpdateProduct() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
		when(cityService.getCityById(1L)).thenReturn(city);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(subcategory);
		when(currencyService.getCurrencyById(1L)).thenReturn(currency);

		// When
		ProductDetailResponseDto result = productService.updateProduct(1L, requestDto);

		// Then
		assertThat(result.productId()).isEqualTo(testProduct.getId());
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("상품 수정 - 실패 (상품 없음)")
	void testUpdateProduct_NotFound() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, requestDto));
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("상품 삭제 - 성공")
	void testDeleteProduct() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

		// When
		productService.deleteProduct(1L);

		// Then
		verify(productRepository).findById(1L);
		verify(productRepository).delete(testProduct);
	}

	@Test
	@DisplayName("상품 삭제 - 실패 (상품 없음)")
	void testDeleteProduct_NotFound() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
		verify(productRepository).findById(1L);
	}
}
