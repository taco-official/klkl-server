package taco.klkl.domain.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.QCategory;
import taco.klkl.domain.category.domain.QSubcategory;
import taco.klkl.domain.category.domain.QTag;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.domain.Tag;
import taco.klkl.domain.category.dto.response.TagResponse;
import taco.klkl.domain.like.exception.LikeCountBelowMinimumException;
import taco.klkl.domain.like.exception.LikeCountOverMaximumException;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductTag;
import taco.klkl.domain.product.domain.QProduct;
import taco.klkl.domain.product.domain.QProductTag;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequest;
import taco.klkl.domain.product.dto.request.ProductFilterOptions;
import taco.klkl.domain.product.dto.request.ProductSortOptions;
import taco.klkl.domain.product.dto.response.ProductDetailResponse;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.CityType;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.CountryType;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.CurrencyType;
import taco.klkl.domain.region.domain.QCity;
import taco.klkl.domain.region.domain.QCountry;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.domain.RegionType;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.UserConstants;
import taco.klkl.global.common.response.PagedResponseDto;
import taco.klkl.global.util.CityUtil;
import taco.klkl.global.util.CurrencyUtil;
import taco.klkl.global.util.SubcategoryUtil;
import taco.klkl.global.util.TagUtil;
import taco.klkl.global.util.UserUtil;

class ProductServiceImplTest {

	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserUtil userUtil;

	@Mock
	private TagUtil tagUtil;

	@Mock
	private SubcategoryUtil subcategoryUtil;

	@Mock
	private CityUtil cityUtil;

	@Mock
	private CurrencyUtil currencyUtil;

	private Product testProduct;
	private User user;
	private City city;
	private Subcategory subcategory;
	private Currency currency;
	private ProductCreateUpdateRequest requestDto;

	private Product mockProduct;

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
			Rating.FIVE,
			user,
			city,
			subcategory,
			currency
		);

		requestDto = new ProductCreateUpdateRequest(
			"productName",
			"productDescription",
			"productAddress",
			1000,
			5.0,
			1L,
			1L,
			1L,
			Set.of(1L, 2L)
		);
		mockProduct = Mockito.mock(Product.class);
	}

	@Test
	@DisplayName("상품 목록 조회 - 성공")
	void testFindProductsByFilterOptionsAndSortOptions() {
		// Given
		Set<Long> cityIds = Set.of(4L, 5L);
		Set<Long> subcategoryIds = Set.of(1L, 2L, 3L);
		Set<Long> tagIds = Set.of(6L, 7L);
		ProductFilterOptions filterOptions = new ProductFilterOptions(
			cityIds,
			subcategoryIds,
			tagIds
		);
		ProductSortOptions sortOptions = new ProductSortOptions(
			"rating",
			"DESC"
		);
		Pageable pageable = PageRequest.of(0, 10);

		// Mocking QueryDSL behavior
		List<Product> productList = List.of(testProduct);

		JPAQuery<Product> baseQuery = mock(JPAQuery.class);
		JPAQuery<Product> productQuery = mock(JPAQuery.class);
		JPAQuery<Long> countQuery = mock(JPAQuery.class);

		QProduct product = QProduct.product;
		QProductTag productTag = QProductTag.productTag;
		QTag tag = QTag.tag;

		when(queryFactory.from(product)).thenReturn((JPAQuery)baseQuery);
		when(baseQuery.leftJoin(product.productTags, productTag)).thenReturn(baseQuery);
		when(baseQuery.leftJoin(productTag.tag, tag)).thenReturn(baseQuery);
		when(baseQuery.where(any(BooleanBuilder.class))).thenReturn(baseQuery);

		when(baseQuery.select(QProduct.product.countDistinct())).thenReturn(countQuery);
		when(countQuery.fetchOne()).thenReturn((long)productList.size());

		when(baseQuery.select(QProduct.product)).thenReturn(productQuery);
		when(productQuery.distinct()).thenReturn(productQuery);
		when(productQuery.offset(pageable.getOffset())).thenReturn(productQuery);
		when(productQuery.limit(pageable.getPageSize())).thenReturn(productQuery);
		when(productQuery.fetch()).thenReturn(productList);

		// Mocking sorting behavior
		PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class, "product");
		when(productQuery.orderBy(any(OrderSpecifier.class))).thenReturn(productQuery);

		// Mocking validation behavior
		Subcategory mockSubcategory = mock(Subcategory.class);
		Tag mockTag = mock(Tag.class);
		when(cityUtil.isCitiesMappedToSameCountry(anySet())).thenReturn(true);
		when(subcategoryUtil.findSubcategoryEntityById(anyLong())).thenReturn(mockSubcategory);
		when(tagUtil.findTagEntityById(anyLong())).thenReturn(mockTag);

		// When
		PagedResponseDto<ProductSimpleResponse> result = productService
			.findProductsByFilterOptionsAndSortOptions(pageable, filterOptions, sortOptions);

		// Then
		assertThat(result.content()).hasSize(1);
		assertThat(result.content().get(0).id()).isEqualTo(testProduct.getId());
		assertThat(result.totalElements()).isEqualTo(1);
		assertThat(result.totalPages()).isEqualTo(1);
		assertThat(result.pageNumber()).isEqualTo(0);
		assertThat(result.pageSize()).isEqualTo(10);
		assertThat(result.last()).isTrue();

		// Verify that the query methods were called
		verify(queryFactory).from(product);
		verify(baseQuery).leftJoin(product.productTags, productTag);
		verify(baseQuery).leftJoin(productTag.tag, tag);
		verify(baseQuery).where(any(BooleanBuilder.class));

		verify(baseQuery).select(QProduct.product.countDistinct());
		verify(countQuery).fetchOne();

		verify(baseQuery).select(QProduct.product);
		verify(productQuery).distinct();
		verify(productQuery).offset(pageable.getOffset());
		verify(productQuery).limit(pageable.getPageSize());
		verify(productQuery).orderBy(any(OrderSpecifier.class));
		verify(productQuery).fetch();

		// Verify that validation methods were called
		verify(cityUtil).isCitiesMappedToSameCountry(cityIds);
		verify(subcategoryUtil, times(3)).findSubcategoryEntityById(anyLong());
		verify(tagUtil, times(2)).findTagEntityById(anyLong());
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void testFindProductById() {
		// Given
		Long productId = 1L;
		when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));

		// When
		ProductDetailResponse result = productService.findProductById(productId);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(testProduct.getId());
		assertThat(result.name()).isEqualTo(testProduct.getName());
		assertThat(result.description()).isEqualTo(testProduct.getDescription());
		assertThat(result.address()).isEqualTo(testProduct.getAddress());
		assertThat(result.price()).isEqualTo(testProduct.getPrice());
		assertThat(result.rating()).isEqualTo(testProduct.getRating().getValue());

		// 필터 검증
		if (testProduct.getProductTags() != null && !testProduct.getProductTags().isEmpty()) {
			assertThat(result.tags()).isNotNull();
			assertThat(result.tags()).hasSize(testProduct.getProductTags().size());
			Set<Long> resultTagIds = result.tags().stream()
				.map(TagResponse::id)
				.collect(Collectors.toSet());

			Set<Long> testProductTagIds = testProduct.getProductTags().stream()
				.map(ProductTag::getTag)
				.map(Tag::getId)
				.collect(Collectors.toSet());

			assertThat(resultTagIds).containsExactlyInAnyOrderElementsOf(testProductTagIds);
		} else {
			assertThat(result.tags()).isEmpty();
		}

		// City, Subcategory, Currency 검증
		assertThat(result.city().id()).isEqualTo(testProduct.getCity().getId());
		assertThat(result.subcategory().id()).isEqualTo(testProduct.getSubcategory().getId());
		assertThat(result.currency().id()).isEqualTo(testProduct.getCurrency().getId());
		verify(productRepository).findById(productId);
	}

	@Test
	@DisplayName("상품 상세 조회 - 실패 (상품 없음)")
	void testFindProductById_NotFound() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProductNotFoundException.class, () -> productService.findProductById(1L));
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("상품 생성 - 성공")
	void testCreateProduct() {
		// Given
		when(userUtil.findTestUser()).thenReturn(user);
		when(cityUtil.findCityEntityById(1L)).thenReturn(city);
		when(subcategoryUtil.findSubcategoryEntityById(1L)).thenReturn(subcategory);
		when(currencyUtil.findCurrencyEntityById(1L)).thenReturn(currency);

		// save 메서드 호출 시 ID를 설정하고 저장된 객체를 반환하도록 설정
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			// ID 설정 (실제로는 데이터베이스에서 생성됨)
			ReflectionTestUtils.setField(savedProduct, "id", 1L);
			return savedProduct;
		});

		// When
		ProductDetailResponse result = productService.createProduct(requestDto);

		// Then
		assertThat(result.id()).isEqualTo(1L);
		verify(productRepository).save(argThat(savedProduct -> {
			assertThat(savedProduct.getId()).isEqualTo(1L);
			assertThat(savedProduct.getName()).isEqualTo(requestDto.name());
			assertThat(savedProduct.getDescription()).isEqualTo(requestDto.description());
			assertThat(savedProduct.getAddress()).isEqualTo(requestDto.address());
			assertThat(savedProduct.getPrice()).isEqualTo(requestDto.price());
			assertThat(savedProduct.getRating().getValue()).isEqualTo(requestDto.rating());
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
		when(cityUtil.findCityEntityById(1L)).thenReturn(city);
		when(subcategoryUtil.findSubcategoryEntityById(1L)).thenReturn(subcategory);
		when(currencyUtil.findCurrencyEntityById(1L)).thenReturn(currency);

		// When
		ProductDetailResponse result = productService.updateProduct(1L, requestDto);

		// Then
		assertThat(result.id()).isEqualTo(testProduct.getId());
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

	@Test
	@DisplayName("상품 좋아요 수 추가 테스트")
	void testAddLikeCount() {
		// given
		when(mockProduct.increaseLikeCount()).thenReturn(1);
		when(mockProduct.getLikeCount()).thenReturn(1);

		// when
		int likeCount = productService.increaseLikeCount(mockProduct);

		// then
		assertThat(mockProduct.getLikeCount()).isEqualTo(likeCount);
	}

	@Test
	@DisplayName("상품 좋아요 수 최대값 에러 테스트")
	void testIncreaseLikeCountMaximum() {
		// given
		when(mockProduct.increaseLikeCount()).thenThrow(LikeCountOverMaximumException.class);

		// when & then
		org.junit.jupiter.api.Assertions.assertThrows(LikeCountOverMaximumException.class, () -> {
			productService.increaseLikeCount(mockProduct);
		});
	}

	@Test
	@DisplayName("상품 좋아요 수 빼기 테스트")
	void testSubtractLikeCount() {
		// given
		testProduct.increaseLikeCount();
		int beforeLikeCount = testProduct.getLikeCount();

		// when
		productService.decreaseLikeCount(testProduct);

		// then
		assertThat(testProduct.getLikeCount()).isEqualTo(beforeLikeCount - 1);
	}

	@Test
	@DisplayName("상품 좋아요 수 최소값 에러 테스트")
	void testIncreaseLikeCountMinimum() {
		// given
		when(mockProduct.decreaseLikeCount()).thenThrow(LikeCountBelowMinimumException.class);

		// when & then
		org.junit.jupiter.api.Assertions.assertThrows(LikeCountBelowMinimumException.class, () -> {
			productService.decreaseLikeCount(mockProduct);
		});
	}

	@Test
	@DisplayName("상품 이름 부분 문자열 조회 테스트")
	void testGetProductsByPartialName() {
		// given
		String partialName = "am";
		List<Product> mockProducts = List.of(testProduct);

		JPAQuery<Product> mockQuery = mock(JPAQuery.class);
		when(queryFactory.selectFrom(QProduct.product)).thenReturn(mockQuery);
		when(mockQuery.join(QProduct.product.city, QCity.city)).thenReturn(mockQuery);
		when(mockQuery.fetchJoin()).thenReturn(mockQuery);
		when(mockQuery.join(QCity.city.country, QCountry.country)).thenReturn(mockQuery);
		when(mockQuery.fetchJoin()).thenReturn(mockQuery);
		when(mockQuery.join(QProduct.product.subcategory, QSubcategory.subcategory)).thenReturn(mockQuery);
		when(mockQuery.fetchJoin()).thenReturn(mockQuery);
		when(mockQuery.join(QSubcategory.subcategory.category, QCategory.category)).thenReturn(mockQuery);
		when(mockQuery.fetchJoin()).thenReturn(mockQuery);
		when(mockQuery.where(QProduct.product.name.contains(partialName))).thenReturn(mockQuery);
		when(mockQuery.fetch()).thenReturn(mockProducts);

		// when
		List<ProductSimpleResponse> responseDtos = productService.getProductsByPartialName(partialName);

		// then
		assertThat(responseDtos.get(0)).isEqualTo(ProductSimpleResponse.from(testProduct));

	}

}
