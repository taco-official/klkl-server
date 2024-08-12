package taco.klkl.domain.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.like.exception.LikeCountBelowMinimumException;
import taco.klkl.domain.like.exception.LikeCountOverMaximumException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;

class ProductTest {

	private User mockUser;
	private City mockCity;
	private Subcategory mockSubcategory;
	private Currency mockCurrency;

	private Product mockProduct;

	@BeforeEach
	public void beforeEach() {
		mockUser = mock(User.class);
		mockCity = mock(City.class);
		mockSubcategory = mock(Subcategory.class);
		mockCurrency = mock(Currency.class);
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

		// when
		Product product = Product.of(
			name,
			description,
			address,
			price,
			mockUser,
			mockCity,
			mockSubcategory,
			mockCurrency
		);
		product.prePersist();

		// then
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(address);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(product.getUser()).isEqualTo(mockUser);
		assertThat(product.getCity()).isEqualTo(mockCity);
		assertThat(product.getSubcategory()).isEqualTo(mockSubcategory);
		assertThat(product.getCurrency()).isEqualTo(mockCurrency);
	}

	@Test
	@DisplayName("주소값이 null인 상품 생성 시 기본값 N/A")
	public void testPrePersistWithoutAddress() {
		// given
		String name = "주소없음";
		String description = "설명";
		String address = null;
		Integer price = 0;

		// when
		Product product = Product.of(
			name,
			description,
			address,
			price,
			mockUser,
			mockCity,
			mockSubcategory,
			mockCurrency
		);
		product.prePersist();

		// then
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(ProductConstants.DEFAULT_ADDRESS);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(product.getUser()).isEqualTo(mockUser);
		assertThat(product.getCity()).isEqualTo(mockCity);
		assertThat(product.getSubcategory()).isEqualTo(mockSubcategory);
		assertThat(product.getCurrency()).isEqualTo(mockCurrency);
	}

	@Test
	@DisplayName("가격이 null인 상품 생성 시 기본값 0")
	public void testPrePersistWithoutPrice() {
		// given
		String name = "가격없음";
		String description = "설명";
		String address = "주소";
		Integer price = null;

		// when
		Product product = Product.of(
			name,
			description,
			address,
			price,
			mockUser,
			mockCity,
			mockSubcategory,
			mockCurrency
		);
		product.prePersist();

		// then
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(address);
		assertThat(product.getPrice()).isEqualTo(ProductConstants.DEFAULT_PRICE);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(product.getUser()).isEqualTo(mockUser);
		assertThat(product.getCity()).isEqualTo(mockCity);
		assertThat(product.getSubcategory()).isEqualTo(mockSubcategory);
		assertThat(product.getCurrency()).isEqualTo(mockCurrency);
	}

	@Test
	@DisplayName("상품 정보 업데이트 테스트")
	public void testUpdate() {
		// given
		String originName = "Origin Name";
		String originDescription = "Original Description";
		String originAddress = "Original Address";
		Integer originPrice = 100;
		City originCity = mockCity;
		Subcategory originSubcategory = mockSubcategory;
		Currency originCurrency = mockCurrency;

		Product product = Product.of(
			originName,
			originDescription,
			originAddress,
			originPrice,
			mockUser,
			originCity,
			originSubcategory,
			originCurrency
		);

		// when
		String updatedName = "Updated Name";
		String updatedDescription = "Updated Description";
		String updatedAddress = "Updated Address";
		Integer updatedPrice = 200;
		City updatedCity = mock(City.class);
		Subcategory updatedSubcategory = mock(Subcategory.class);
		Currency updatedCurrency = mock(Currency.class);

		product.update(
			updatedName,
			updatedDescription,
			updatedAddress,
			updatedPrice,
			updatedCity,
			updatedSubcategory,
			updatedCurrency
		);

		// then
		assertThat(product.getName()).isEqualTo(updatedName);
		assertThat(product.getDescription()).isEqualTo(updatedDescription);
		assertThat(product.getAddress()).isEqualTo(updatedAddress);
		assertThat(product.getPrice()).isEqualTo(updatedPrice);
		assertThat(product.getCity()).isEqualTo(updatedCity);
		assertThat(product.getSubcategory()).isEqualTo(updatedSubcategory);
		assertThat(product.getCurrency()).isEqualTo(updatedCurrency);
	}

	@Test
	@DisplayName("상품 좋아요수 증가 테스트")
	public void testIncreaseLikeCount() {
		// given
		Product product = Product.of(
			"mockUser",
			"Original Name",
			"Original Description",
			100,
			mockUser,
			mockCity,
			mockSubcategory,
			mockCurrency);
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
		when(mockProduct.increaseLikeCount()).thenThrow(LikeCountOverMaximumException.class);

		// when & then
		Assertions.assertThrows(LikeCountOverMaximumException.class, mockProduct::increaseLikeCount);
	}

	@Test
	@DisplayName("상품 좋아요수 감소 테스트")
	public void testDecreaseLikeCount() {
		// given
		Product product = Product.of(
			"name",
			"Original Description",
			"Original address",
			100,
			mockUser,
			mockCity,
			mockSubcategory,
			mockCurrency);
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
		when(mockProduct.increaseLikeCount()).thenThrow(LikeCountBelowMinimumException.class);

		// when & then
		Assertions.assertThrows(LikeCountBelowMinimumException.class, mockProduct::increaseLikeCount);
	}
}
