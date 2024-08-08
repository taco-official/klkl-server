package taco.klkl.domain.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import taco.klkl.domain.like.exception.LikeCountMaximumException;
import taco.klkl.domain.like.exception.LikeCountMinimumException;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;

class ProductTest {

	private User user;

	private Product mockProduct;

	@BeforeEach
	public void beforeEach() {
		user = Mockito.mock(User.class);
		mockProduct = Mockito.mock(Product.class);
	}

	@Test
	@DisplayName("필드값이 모두 채워진 상품 생성 시 입력한 값과 동일")
	public void testPrePersistWithFilledProduct() {
		// given
		String name = "나성공";
		String description = "나성공 설명";
		String address = "나성공 주소";
		Integer price = 0;
		Long cityId = 100L;
		Long subcategoryId = 200L;
		Long currencyId = 300L;

		// when
		Product product = Product.of(user, name, description, address, price, cityId, subcategoryId, currencyId);
		product.prePersist();

		// then
		assertThat(product.getUser()).isEqualTo(user);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(address);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getCityId()).isEqualTo(cityId);
		assertThat(product.getSubcategoryId()).isEqualTo(subcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(currencyId);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
	}

	@Test
	@DisplayName("주소값이 null인 상품 생성 시 기본값 N/A")
	public void testPrePersistWithoutAddress() {
		// given
		String name = "주소없음";
		String description = "설명";
		String address = null;
		Integer price = 0;
		Long cityId = 1L;
		Long subcategoryId = 1L;
		Long currencyId = 1L;

		// when
		Product product = Product.of(user, name, description, address, price, cityId, subcategoryId, currencyId);
		product.prePersist();

		// then
		assertThat(product.getUser()).isEqualTo(user);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(ProductConstants.DEFAULT_ADDRESS);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getCityId()).isEqualTo(cityId);
		assertThat(product.getSubcategoryId()).isEqualTo(subcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(currencyId);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
	}

	@Test
	@DisplayName("가격이 null인 상품 생성 시 기본값 0")
	public void testPrePersistWithoutPrice() {
		// given
		String name = "가격없음";
		String description = "설명";
		String address = "주소";
		Integer price = null;
		Long cityId = 1L;
		Long subcategoryId = 1L;
		Long currencyId = 1L;

		// when
		Product product = Product.of(user, name, description, address, price, cityId, subcategoryId, currencyId);
		product.prePersist();

		// then
		assertThat(product.getUser()).isEqualTo(user);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(address);
		assertThat(product.getPrice()).isEqualTo(ProductConstants.DEFAULT_PRICE);
		assertThat(product.getCityId()).isEqualTo(cityId);
		assertThat(product.getSubcategoryId()).isEqualTo(subcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(currencyId);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
	}

	@Test
	@DisplayName("상품 정보 업데이트 테스트")
	public void testUpdate() {
		// given
		Product product = Product.of(
			user,
			"Original Name",
			"Original Description",
			"Original Address",
			100,
			1L,
			1L,
			1L
		);
		ProductUpdateRequestDto updateDto = new ProductUpdateRequestDto(
			"Updated Name",
			"Updated Description",
			"Updated Address",
			200,
			2L,
			2L,
			2L
		);

		// when
		product.update(updateDto);

		// then
		assertThat(product.getName()).isEqualTo(updateDto.name());
		assertThat(product.getDescription()).isEqualTo(updateDto.description());
		assertThat(product.getAddress()).isEqualTo(updateDto.address());
		assertThat(product.getPrice()).isEqualTo(updateDto.price());
		assertThat(product.getCityId()).isEqualTo(updateDto.cityId());
		assertThat(product.getSubcategoryId()).isEqualTo(updateDto.subcategoryId());
		assertThat(product.getCurrencyId()).isEqualTo(updateDto.currencyId());
	}

	@Test
	@DisplayName("상품 정보 부분 업데이트 테스트")
	public void testPartialUpdate() {
		// given
		Product product = Product.of(
			user,
			"Original Name",
			"Original Description",
			"Original Address",
			100,
			1L,
			1L,
			1L
		);
		ProductUpdateRequestDto updateDto = new ProductUpdateRequestDto(
			null,
			"Updated Description",
			null,
			200,
			null,
			2L,
			null
		);

		// when
		product.update(updateDto);

		// then
		assertThat(product.getName()).isEqualTo("Original Name");
		assertThat(product.getDescription()).isEqualTo(updateDto.description());
		assertThat(product.getAddress()).isEqualTo("Original Address");
		assertThat(product.getPrice()).isEqualTo(updateDto.price());
		assertThat(product.getCityId()).isEqualTo(1L);
		assertThat(product.getSubcategoryId()).isEqualTo(updateDto.subcategoryId());
		assertThat(product.getCurrencyId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("상품 정보 업데이트 시 null 값 무시 테스트")
	public void testUpdateIgnoreNullValues() {
		// given
		Product product = Product.of(
			user,
			"Original Name",
			"Original Description",
			"Original Address",
			100,
			1L,
			1L,
			1L);
		ProductUpdateRequestDto updateDto = new ProductUpdateRequestDto(
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);

		// when
		product.update(updateDto);

		// then
		assertThat(product.getName()).isEqualTo("Original Name");
		assertThat(product.getDescription()).isEqualTo("Original Description");
		assertThat(product.getAddress()).isEqualTo("Original Address");
		assertThat(product.getPrice()).isEqualTo(100);
		assertThat(product.getCityId()).isEqualTo(1L);
		assertThat(product.getSubcategoryId()).isEqualTo(1L);
		assertThat(product.getCurrencyId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("상품 좋아요수 증가 테스트")
	public void testIncreaseLikeCount() {
		// given
		Product product = Product.of(
			user,
			"Original Name",
			"Original Description",
			"Original Address",
			100,
			1L,
			1L,
			1L);
		int beforeLikeCount = product.getLikeCount();

		// when
		product.increaseLikeCount();

		// then
		assertThat(product.getLikeCount()).isEqualTo(beforeLikeCount + 1);
	}

	@Test
	@DisplayName("상품 좋아요수 최대값 에러 테스트")
	public void testIncreaseLikeCountMaximum() {
		// given
		when(mockProduct.increaseLikeCount()).thenThrow(LikeCountMaximumException.class);

		// when & then
		Assertions.assertThrows(LikeCountMaximumException.class, mockProduct::increaseLikeCount);
	}

	@Test
	@DisplayName("상품 좋아요수 감소 테스트")
	public void testDecreaseLikeCount() {
		// given
		Product product = Product.of(
			user,
			"Original Name",
			"Original Description",
			"Original Address",
			100,
			1L,
			1L,
			1L);
		product.increaseLikeCount();
		int beforeLikeCount = product.getLikeCount();

		// when
		product.decreaseLikeCount();

		// then
		assertThat(product.getLikeCount()).isEqualTo(beforeLikeCount - 1);
	}

	@Test
	@DisplayName("상품 좋아요수 최소값 에러 테스트")
	public void testDecreaseLikeCountMinimum() {
		// given
		when(mockProduct.increaseLikeCount()).thenThrow(LikeCountMinimumException.class);

		// when & then
		Assertions.assertThrows(LikeCountMinimumException.class, mockProduct::increaseLikeCount);
	}
}
