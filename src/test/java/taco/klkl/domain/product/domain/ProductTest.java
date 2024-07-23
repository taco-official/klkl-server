package taco.klkl.domain.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import taco.klkl.domain.user.domain.User;

class ProductTest {
	private static final Long DEFAULT_PRICE = 0L;
	private static final Long DEFAULT_LIKE_COUNT = 0L;
	private static final String DEFAULT_ADDRESS = "N/A";

	private User user;

	@BeforeEach
	public void beforeEach() {
		user = Mockito.mock(User.class);
	}

	@Test
	@DisplayName("필드값이 모두 채워진 상품 생성 시 입력한 값과 동일")
	public void testPrePersistWithFilledProduct() {
		// given
		String name = "나성공";
		String description = "나성공 설명";
		String address = "나성공 주소";
		Long price = 2147483648L;
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
		assertThat(product.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
	}

	@Test
	@DisplayName("주소값이 null인 상품 생성 시 기본값 N/A")
	public void testPrePersistWithoutAddress() {
		// given
		String name = "주소없음";
		String description = "설명";
		String address = null;
		Long price = 0L;
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
		assertThat(product.getAddress()).isEqualTo(DEFAULT_ADDRESS);
		assertThat(product.getPrice()).isEqualTo(price);
		assertThat(product.getCityId()).isEqualTo(cityId);
		assertThat(product.getSubcategoryId()).isEqualTo(subcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(currencyId);
		assertThat(product.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
	}

	@Test
	@DisplayName("가격이 null인 상품 생성 시 기본값 0L")
	public void testPrePersistWithoutPrice() {
		// given
		Long defaultPrice = 0L;
		String name = "가격없음";
		String description = "설명";
		String address = "주소";
		Long price = null;
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
		assertThat(product.getPrice()).isEqualTo(DEFAULT_PRICE);
		assertThat(product.getCityId()).isEqualTo(cityId);
		assertThat(product.getSubcategoryId()).isEqualTo(subcategoryId);
		assertThat(product.getCurrencyId()).isEqualTo(currencyId);
		assertThat(product.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
	}
}
