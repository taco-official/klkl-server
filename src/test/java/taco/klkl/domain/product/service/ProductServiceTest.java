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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
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
import taco.klkl.global.util.UserUtil;

class ProductServiceTest {

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

	private Product product;
	private User mockUser;
	private City city;
	private Subcategory subcategory;
	private Currency currency;
	private ProductCreateUpdateRequestDto requestDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);
		when(mockUser.getName()).thenReturn("Test User");

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

		product = mock(Product.class);
		when(product.getId()).thenReturn(1L);
		when(product.getName()).thenReturn("productName");
		when(product.getDescription()).thenReturn("productDescription");
		when(product.getAddress()).thenReturn("productAddress");
		when(product.getPrice()).thenReturn(1000);
		when(product.getLikeCount()).thenReturn(0);
		when(product.getUser()).thenReturn(mockUser);
		when(product.getCity()).thenReturn(city);
		when(product.getSubcategory()).thenReturn(subcategory);
		when(product.getCurrency()).thenReturn(currency);

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
	void testGetProducts() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		List<Product> productList = Arrays.asList(product);
		Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());
		when(productRepository.findAll(pageable)).thenReturn(productPage);

		// When
		PagedResponseDto<ProductSimpleResponseDto> result = productService.getProducts(pageable);

		// Then
		assertThat(result.content()).hasSize(1);
		assertThat(result.content().get(0).productId()).isEqualTo(product.getId());
		assertThat(result.totalElements()).isEqualTo(1);
		assertThat(result.totalPages()).isEqualTo(1);
		assertThat(result.pageNumber()).isEqualTo(0);
		assertThat(result.pageSize()).isEqualTo(10);
		assertThat(result.last()).isTrue();
		verify(productRepository).findAll(pageable);
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void testGetProductById() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		// When
		ProductDetailResponseDto result = productService.getProductById(1L);

		// Then
		assertThat(result.productId()).isEqualTo(product.getId());
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
		when(userUtil.findTestUser()).thenReturn(mockUser);
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
			assertThat(savedProduct.getUser()).isEqualTo(mockUser);
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
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(cityService.getCityById(1L)).thenReturn(city);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(subcategory);
		when(currencyService.getCurrencyById(1L)).thenReturn(currency);

		// When
		ProductDetailResponseDto result = productService.updateProduct(1L, requestDto);

		// Then
		assertThat(result.productId()).isEqualTo(product.getId());
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
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		// When
		productService.deleteProduct(1L);

		// Then
		verify(productRepository).findById(1L);
		verify(productRepository).delete(product);
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
