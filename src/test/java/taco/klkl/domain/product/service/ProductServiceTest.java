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

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CurrencyType;
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

	private Product mockProduct;
	private User mockUser;
	private City mockCity;
	private Subcategory mockSubcategory;
	private Currency mockCurrency;
	private ProductCreateUpdateRequestDto requestDto;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);
		when(mockUser.getName()).thenReturn("Test User");

		mockCity = mock(City.class);
		when(mockCity.getCityId()).thenReturn(1L);
		when(mockCity.getName()).thenReturn(CityType.BANGKOK);

		mockSubcategory = mock(Subcategory.class);
		when(mockSubcategory.getId()).thenReturn(1L);
		when(mockSubcategory.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);

		mockCurrency = mock(Currency.class);
		when(mockCurrency.getCurrencyId()).thenReturn(1L);
		when(mockCurrency.getCode()).thenReturn(CurrencyType.THAI_BAHT);
		when(mockCurrency.getFlag()).thenReturn("image/flag.jpg");

		mockProduct = mock(Product.class);
		when(mockProduct.getId()).thenReturn(1L);
		when(mockProduct.getName()).thenReturn("productName");
		when(mockProduct.getDescription()).thenReturn("productDescription");
		when(mockProduct.getAddress()).thenReturn("productAddress");
		when(mockProduct.getPrice()).thenReturn(1000);
		when(mockProduct.getLikeCount()).thenReturn(0);
		when(mockProduct.getUser()).thenReturn(mockUser);
		when(mockProduct.getCity()).thenReturn(mockCity);
		when(mockProduct.getSubcategory()).thenReturn(mockSubcategory);
		when(mockProduct.getCurrency()).thenReturn(mockCurrency);

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
	void testGetAllProducts() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		Page<Product> productPage = new PageImpl<>(Arrays.asList(mockProduct));
		when(productRepository.findAll(pageable)).thenReturn(productPage);

		// When
		List<ProductSimpleResponseDto> result = productService.getAllProducts(pageable);

		// Then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).productId()).isEqualTo(mockProduct.getId());
		verify(productRepository).findAll(pageable);
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void testGetProductById() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

		// When
		ProductDetailResponseDto result = productService.getProductById(1L);

		// Then
		assertThat(result.productId()).isEqualTo(mockProduct.getId());
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
		when(cityService.getCityById(1L)).thenReturn(mockCity);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(mockSubcategory);
		when(currencyService.getCurrencyById(1L)).thenReturn(mockCurrency);

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
			assertThat(savedProduct.getCity()).isEqualTo(mockCity);
			assertThat(savedProduct.getSubcategory()).isEqualTo(mockSubcategory);
			assertThat(savedProduct.getCurrency()).isEqualTo(mockCurrency);
			return true;
		}));
	}

	@Test
	@DisplayName("상품 수정 - 성공")
	void testUpdateProduct() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
		when(cityService.getCityById(1L)).thenReturn(mockCity);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(mockSubcategory);
		when(currencyService.getCurrencyById(1L)).thenReturn(mockCurrency);

		// When
		ProductDetailResponseDto result = productService.updateProduct(1L, requestDto);

		// Then
		assertThat(result.productId()).isEqualTo(mockProduct.getId());
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
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

		// When
		productService.deleteProduct(1L);

		// Then
		verify(productRepository).findById(1L);
		verify(productRepository).delete(mockProduct);
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
