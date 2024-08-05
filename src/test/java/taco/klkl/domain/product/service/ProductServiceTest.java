package taco.klkl.domain.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.service.CityServiceImpl;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.util.UserUtil;

class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CityServiceImpl cityService;

	@Mock
	private SubcategoryService subcategoryService;

	@Mock
	private UserUtil userUtil;

	private Product mockProduct;
	private User mockUser;
	private City mockCity;
	private Subcategory mockSubcategory;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);

		mockCity = mock(City.class);
		when(mockCity.getCityId()).thenReturn(1L);

		mockSubcategory = mock(Subcategory.class);
		when(mockSubcategory.getId()).thenReturn(1L);

		mockProduct = mock(Product.class);
		when(mockProduct.getProductId()).thenReturn(1L);
		when(mockProduct.getUser()).thenReturn(mockUser);
		when(mockProduct.getName()).thenReturn("Test Product");
		when(mockProduct.getDescription()).thenReturn("Test Description");
		when(mockProduct.getAddress()).thenReturn("Test Address");
		when(mockProduct.getLikeCount()).thenReturn(ProductConstants.DEFAULT_LIKE_COUNT);
		when(mockProduct.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(mockProduct.getPrice()).thenReturn(1000);
		when(mockProduct.getCity()).thenReturn(mockCity);
		when(mockProduct.getSubcategory()).thenReturn(mockSubcategory);
		when(mockProduct.getCurrencyId()).thenReturn(1L);
	}

	@Test
	@DisplayName("모든 상품 조회 테스트")
	void testGetAllProducts() {
		// Given
		Pageable pageable = PageRequest.of(0, 10);
		List<Product> productList = Arrays.asList(mockProduct);
		Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());
		when(productRepository.findAll(pageable)).thenReturn(productPage);

		// When
		List<ProductSimpleResponseDto> result = productService.getAllProducts(pageable);

		// Then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).productId()).isEqualTo(mockProduct.getProductId());
		verify(productRepository).findAll(pageable);
	}

	@Test
	@DisplayName("ID로 상품 조회 테스트")
	void testGetProductById() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

		// When
		ProductDetailResponseDto result = productService.getProductById(1L);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.productId()).isEqualTo(mockProduct.getProductId());
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("존재하지 않는 상품 ID로 조회 시 예외 발생 테스트")
	void testGetProductByIdNotFound() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
		verify(productRepository).findById(1L);
	}

	@Test
	@DisplayName("상품 생성 테스트")
	void testCreateProduct() {
		// Given
		ProductCreateRequestDto requestDto = new ProductCreateRequestDto(
			mockProduct.getName(),
			mockProduct.getDescription(),
			mockProduct.getAddress(),
			mockProduct.getPrice(),
			mockProduct.getCity().getCityId(),
			mockProduct.getSubcategory().getId(),
			mockProduct.getCurrencyId()
		);
		User mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);

		City mockCity = mock(City.class);
		when(mockCity.getCityId()).thenReturn(1L);

		Subcategory mockSubcategory = mock(Subcategory.class);
		when(mockSubcategory.getId()).thenReturn(1L);

		when(userUtil.findTestUser()).thenReturn(mockUser);
		when(cityService.getCityById(1L)).thenReturn(mockCity);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(mockSubcategory);

		// When
		ProductDetailResponseDto result = productService.createProduct(requestDto);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo(mockProduct.getName());
		assertThat(result.description()).isEqualTo(mockProduct.getDescription());
		assertThat(result.address()).isEqualTo(mockProduct.getAddress());
		assertThat(result.price()).isEqualTo(mockProduct.getPrice());
		assertThat(result.cityId()).isEqualTo(mockProduct.getCity().getCityId());
		assertThat(result.subcategoryId()).isEqualTo(mockProduct.getSubcategory().getId());
		assertThat(result.currencyId()).isEqualTo(mockProduct.getCurrencyId());

		verify(userUtil).findTestUser();
		verify(cityService).getCityById(1L);
		verify(productRepository).save(any(Product.class));
	}

	@Test
	@DisplayName("상품 수정 테스트")
	void testUpdateProduct() {
		// Given
		ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto(
			"Updated Product",
			"Updated Description",
			"Updated Address",
			2000,
			1L,
			1L,
			2L
		);
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
		when(cityService.getCityById(1L)).thenReturn(mockCity);
		when(subcategoryService.getSubcategoryById(1L)).thenReturn(mockSubcategory);

		// When
		ProductDetailResponseDto result = productService.updateProduct(1L, requestDto);

		// Then
		assertThat(result).isNotNull();
		verify(productRepository).findById(1L);
		verify(cityService).getCityById(1L);
		verify(mockProduct).update(
			requestDto.name(),
			requestDto.description(),
			requestDto.address(),
			requestDto.price(),
			mockCity,
			mockSubcategory,
			requestDto.currencyId()
		);
	}

	@Test
	@DisplayName("상품 삭제 테스트")
	void testDeleteProduct() {
		// Given
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

		// When
		productService.deleteProduct(1L);

		// Then
		verify(productRepository).findById(1L);
		verify(productRepository).delete(mockProduct);
	}
}
