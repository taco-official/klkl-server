package taco.klkl.domain.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.util.UserUtil;

class ProductServiceTest {
	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserUtil userUtil;

	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User();
	}

	@Test
	@DisplayName("ID로 상품 정보를 조회할 때, 상품이 존재하면 ProductDetailResponseDto를 반환해야 한다.")
	void testGetProductInfoById_Success() {
		// given
		Long productId = 1L;

		Product product = ProductConstants.TEST_PRODUCT;
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		ProductDetailResponseDto responseDto = productService.getProductInfoById(productId);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.userId()).isEqualTo(user.getId());
		assertThat(responseDto.name()).isEqualTo(product.getName());
		assertThat(responseDto.description()).isEqualTo(product.getDescription());
		assertThat(responseDto.address()).isEqualTo(product.getAddress());
		assertThat(responseDto.likeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(responseDto.price()).isEqualTo(product.getPrice());
		assertThat(responseDto.cityId()).isEqualTo(product.getCityId());
		assertThat(responseDto.subcategoryId()).isEqualTo(product.getSubcategoryId());
		assertThat(responseDto.currencyId()).isEqualTo(product.getCurrencyId());

		verify(productRepository).findById(productId);
	}

	@Test
	@DisplayName("ID로 상품 정보를 조회할 때, 상품이 존재하지 않으면 ProductNotFoundException을 발생시켜야 한다.")
	void testGetProductInfoById_NotFound() {
		// given
		Long productId = 1L;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
			productService.getProductInfoById(productId);
		});

		// 예외가 발생했는지 확인
		assertThat(exception).isNotNull();
		verify(productRepository).findById(productId);
	}

	@Test
	@DisplayName("상품을 생성할 때, 정상적으로 ProductDetailResponseDto를 반환해야 한다.")
	void testCreateProduct_Success() {
		// given
		Product product = ProductConstants.TEST_PRODUCT;
		ProductCreateRequestDto requestDto = new ProductCreateRequestDto(
			product.getName(),
			product.getDescription(),
			product.getAddress(),
			product.getPrice(),
			product.getCityId(),
			product.getSubcategoryId(),
			product.getCurrencyId()
		);

		when(userUtil.findTestUser()).thenReturn(user);
		when(productRepository.save(any(Product.class))).thenReturn(product);

		// when
		ProductDetailResponseDto responseDto = productService.createProduct(requestDto);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.userId()).isEqualTo(user.getId());
		assertThat(responseDto.name()).isEqualTo(requestDto.name());
		assertThat(responseDto.description()).isEqualTo(requestDto.description());
		assertThat(responseDto.address()).isEqualTo(requestDto.address());
		assertThat(responseDto.likeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(responseDto.price()).isEqualTo(requestDto.price());
		assertThat(responseDto.cityId()).isEqualTo(requestDto.cityId());
		assertThat(responseDto.subcategoryId()).isEqualTo(requestDto.subcategoryId());
		assertThat(responseDto.currencyId()).isEqualTo(requestDto.currencyId());

		verify(productRepository).save(any(Product.class));
	}

	@Test
	@DisplayName("상품 업데이트 성공 테스트")
	void testUpdateProduct_Success() {
		// given
		Long productId = 1L;
		Product existingProduct = ProductConstants.TEST_PRODUCT;

		ProductUpdateRequestDto updateDto = new ProductUpdateRequestDto(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			200,
			2L,
			2L,
			2L
		);

		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		ProductDetailResponseDto responseDto = productService.updateProduct(productId, updateDto);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.name()).isEqualTo(updateDto.name());
		assertThat(responseDto.description()).isEqualTo(updateDto.description());
		assertThat(responseDto.address()).isEqualTo(updateDto.address());
		assertThat(responseDto.price()).isEqualTo(updateDto.price());
		assertThat(responseDto.cityId()).isEqualTo(updateDto.cityId());
		assertThat(responseDto.subcategoryId()).isEqualTo(updateDto.subcategoryId());
		assertThat(responseDto.currencyId()).isEqualTo(updateDto.currencyId());

		verify(productRepository).findById(productId);
	}

	@Test
	@DisplayName("상품 업데이트 실패 테스트 - 상품 없음")
	void testUpdateProduct_NotFound() {
		// given
		Long productId = 1L;
		ProductUpdateRequestDto updateDto = new ProductUpdateRequestDto(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			200,
			2L,
			2L,
			2L
		);

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
			productService.updateProduct(productId, updateDto);
		});

		// 예외가 발생했는지 확인
		assertThat(exception).isNotNull();
		verify(productRepository).findById(productId);
		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	@DisplayName("존재하는 제품 삭제 성공")
	void deleteExistingProduct() {
		// Given
		Long productId = 1L;
		Product product = ProductConstants.TEST_PRODUCT;
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// When
		assertDoesNotThrow(() -> productService.deleteProduct(productId));

		// Then
		verify(productRepository).findById(productId);
		verify(productRepository).delete(product);
	}

	@Test
	@DisplayName("존재하지 않는 제품 삭제 시 예외 발생")
	void deleteNonExistingProduct() {
		// Given
		Long nonExistingProductId = 999L;
		when(productRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(nonExistingProductId));
		verify(productRepository).findById(nonExistingProductId);
		verify(productRepository, never()).delete(any(Product.class));
	}

	@Test
	@DisplayName("제품 삭제 시 repository 메소드 호출 확인")
	void verifyRepositoryMethodsCalledOnDelete() {
		// Given
		Long productId = 1L;
		Product product = ProductConstants.TEST_PRODUCT;
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// When
		productService.deleteProduct(productId);

		// Then
		verify(productRepository).findById(productId);
		verify(productRepository).delete(product);
	}

	@Test
	@DisplayName("상품 좋아요수 추가 테스트")
	void testAddLikeCount() {
		// given
		Product product = ProductConstants.TEST_PRODUCT;
		int beforeLikeCount = product.getLikeCount();

		// when
		productService.increaseLikeCount(product);

		// then
		Assertions.assertThat(product.getLikeCount()).isEqualTo(beforeLikeCount + 1);
	}

	@Test
	@DisplayName("상품 좋아요수 빼기 테스트")
	void testSubtractLikeCount() {
		// given
		Product product = ProductConstants.TEST_PRODUCT;
		product.increaseLikeCount();
		int beforeLikeCount = product.getLikeCount();

		// when
		productService.decreaseLikeCount(product);

		// then
		Assertions.assertThat(product.getLikeCount()).isEqualTo(beforeLikeCount - 1);
	}

}
