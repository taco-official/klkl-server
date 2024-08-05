package taco.klkl.domain.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;

class ProductTest {

	private User mockUser;
	private City mockCity;

	@BeforeEach
	public void beforeEach() {
		mockUser = mock(User.class);
		mockCity = mock(City.class);
	}

	@Test
	@DisplayName("필드값이 모두 채워진 상품 생성 시 입력한 값과 동일")
	public void testPrePersistWithFilledProduct() {
		// given
		String name = "나성공";
		String description = "나성공 설명";
		String address = "나성공 주소";
		Integer price = 0;
		Long subcategoryId = 200L;
		Long currencyId = 300L;

		// when
		Product product = Product.of(mockUser, name, description, address, price, mockCity, subcategoryId, currencyId);
		product.prePersist();

		// then
		assertThat(product.getUser()).isEqualTo(mockUser);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(address);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getCity()).isEqualTo(mockCity);
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
		Long subcategoryId = 1L;
		Long currencyId = 1L;

		// when
		Product product = Product.of(
			mockUser,
			name,
			description,
			address,
			price,
			mockCity,
			subcategoryId,
			currencyId
		);
		product.prePersist();

		// then
		assertThat(product.getUser()).isEqualTo(mockUser);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(ProductConstants.DEFAULT_ADDRESS);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getCity()).isEqualTo(mockCity);
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
		Long subcategoryId = 1L;
		Long currencyId = 1L;

		// when
		Product product = Product.of(
			mockUser,
			name,
			description,
			address,
			price,
			mockCity,
			subcategoryId,
			currencyId
		);
		product.prePersist();

		// then
		assertThat(product.getUser()).isEqualTo(mockUser);
		assertThat(product.getName()).isEqualTo(name);
		assertThat(product.getDescription()).isEqualTo(description);
		assertThat(product.getAddress()).isEqualTo(address);
		assertThat(product.getPrice()).isEqualTo(ProductConstants.DEFAULT_PRICE);
		assertThat(product.getCity()).isEqualTo(mockCity);
		assertThat(product.getSubcategoryId()).isEqualTo(subcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(currencyId);
		assertThat(product.getLikeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
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
		Long originSubcategoryId = 1L;
		Long originCurrencyId = 1L;

		Product product = Product.of(
			mockUser,
			originName,
			originDescription,
			originAddress,
			originPrice,
			originCity,
			originSubcategoryId,
			originCurrencyId
		);

		// when
		String updatedName = "Updated Name";
		String updatedDescription = "Updated Description";
		String updatedAddress = "Updated Address";
		Integer updatedPrice = 200;
		City updatedCity = mock(City.class);
		Long updatedSubcategoryId = 2L;
		Long updatedCurrencyId = 2L;
		product.update(
			updatedName,
			updatedDescription,
			updatedAddress,
			updatedPrice,
			updatedCity,
			updatedSubcategoryId,
			updatedCurrencyId
		);

		// then
		assertThat(product.getName()).isEqualTo(updatedName);
		assertThat(product.getDescription()).isEqualTo(updatedDescription);
		assertThat(product.getAddress()).isEqualTo(updatedAddress);
		assertThat(product.getPrice()).isEqualTo(updatedPrice);
		assertThat(product.getCity()).isEqualTo(updatedCity);
		assertThat(product.getSubcategoryId()).isEqualTo(updatedSubcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(updatedCurrencyId);
	}

	@Test
	@DisplayName("상품 정보 부분 업데이트 테스트")
	public void testPartialUpdate() {
		// given
		String originName = "Origin Name";
		String originDescription = "Original Description";
		String originAddress = "Original Address";
		Integer originPrice = 100;
		City originCity = mockCity;
		Long originSubcategoryId = 1L;
		Long originCurrencyId = 1L;

		Product product = Product.of(
			mockUser,
			originName,
			originDescription,
			originAddress,
			originPrice,
			originCity,
			originSubcategoryId,
			originCurrencyId
		);

		// when
		product.update(
			null,
			"Updated Description",
			null,
			200,
			null,
			2L,
			null
		);

		// then
		assertThat(product.getName()).isEqualTo(originName);
		assertThat(product.getDescription()).isEqualTo("Updated Description");
		assertThat(product.getAddress()).isEqualTo(originAddress);
		assertThat(product.getPrice()).isEqualTo(200);
		assertThat(product.getCity()).isEqualTo(originCity);
		assertThat(product.getSubcategoryId()).isEqualTo(2L);
		assertThat(product.getCurrencyId()).isEqualTo(originCurrencyId);
	}

	@Test
	@DisplayName("상품 정보 업데이트 시 null 값 무시 테스트")
	public void testUpdateIgnoreNullValues() {
		// given
		String originName = "Origin Name";
		String originDescription = "Original Description";
		String originAddress = "Original Address";
		Integer originPrice = 100;
		City originCity = mockCity;
		Long originSubcategoryId = 1L;
		Long originCurrencyId = 1L;

		Product product = Product.of(
			mockUser,
			originName,
			originDescription,
			originAddress,
			originPrice,
			originCity,
			originSubcategoryId,
			originCurrencyId
		);

		// when
		product.update(
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);

		// then
		assertThat(product.getName()).isEqualTo(originName);
		assertThat(product.getDescription()).isEqualTo(originDescription);
		assertThat(product.getAddress()).isEqualTo(originAddress);
		assertThat(product.getPrice()).isEqualTo(originPrice);
		assertThat(product.getCity()).isEqualTo(originCity);
		assertThat(product.getSubcategoryId()).isEqualTo(originSubcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(originCurrencyId);
	}
}
