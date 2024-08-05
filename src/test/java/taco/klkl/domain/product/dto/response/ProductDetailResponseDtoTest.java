package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.user.domain.User;

class ProductDetailResponseDtoTest {
	@Test
	@DisplayName("Product 객체로부터 ProductDetailResponseDto 생성 테스트")
	void testFromProduct() {
		// given
		Long productId = 1L;
		User mockUser = mock(User.class);
		String name = "맛있는 곤약젤리";
		String description = "탱글탱글 맛있는 곤약젤리";
		String address = "신사이바시 메가돈키호테";
		int likeCount = 10;
		LocalDateTime createdAt = LocalDateTime.now();
		int price = 1000;
		City mockCity = mock(City.class);
		Subcategory mockSubcategory = mock(Subcategory.class);
		Currency mockCurrency = mock(Currency.class);

		Product product = mock(Product.class);
		when(product.getId()).thenReturn(productId);
		when(product.getUser()).thenReturn(mockUser);
		when(product.getName()).thenReturn(name);
		when(product.getDescription()).thenReturn(description);
		when(product.getAddress()).thenReturn(address);
		when(product.getLikeCount()).thenReturn(likeCount);
		when(product.getCreatedAt()).thenReturn(createdAt);
		when(product.getPrice()).thenReturn(price);
		when(product.getCity()).thenReturn(mockCity);
		when(product.getSubcategory()).thenReturn(mockSubcategory);
		when(product.getCurrency()).thenReturn(mockCurrency);

		// when
		ProductDetailResponseDto dto = ProductDetailResponseDto.from(product);

		// then
		assertThat(dto.productId()).isEqualTo(productId);
		assertThat(dto.userId()).isEqualTo(mockUser.getId());
		assertThat(dto.name()).isEqualTo(name);
		assertThat(dto.description()).isEqualTo(description);
		assertThat(dto.address()).isEqualTo(address);
		assertThat(dto.likeCount()).isEqualTo(likeCount);
		assertThat(dto.createdAt()).isEqualTo(createdAt);
		assertThat(dto.price()).isEqualTo(price);
		assertThat(dto.cityId()).isEqualTo(mockCity.getCityId());
		assertThat(dto.subcategoryId()).isEqualTo(mockSubcategory.getId());
		assertThat(dto.currencyId()).isEqualTo(mockCurrency.getCurrencyId());
	}

	@Test
	@DisplayName("ProductDetailResponseDto 생성자 테스트")
	void testConstructor() {
		// given
		Long productId = 1L;
		User mockUser = mock(User.class);
		String name = "맛있는 곤약젤리";
		String description = "탱글탱글 맛있는 곤약젤리";
		String address = "신사이바시 메가돈키호테";
		int likeCount = 10;
		LocalDateTime createdAt = LocalDateTime.now();
		int price = 1000;
		City mockCity = mock(City.class);
		Subcategory mockSubcategory = mock(Subcategory.class);
		Currency mockCurrency = mock(Currency.class);

		// when
		ProductDetailResponseDto dto = new ProductDetailResponseDto(
			productId,
			mockUser.getId(),
			name,
			description,
			address,
			likeCount,
			createdAt,
			price,
			mockCity.getCityId(),
			mockSubcategory.getId(),
			mockCurrency.getCurrencyId()
		);

		// then
		assertThat(dto.productId()).isEqualTo(productId);
		assertThat(dto.userId()).isEqualTo(mockUser.getId());
		assertThat(dto.name()).isEqualTo(name);
		assertThat(dto.description()).isEqualTo(description);
		assertThat(dto.address()).isEqualTo(address);
		assertThat(dto.likeCount()).isEqualTo(likeCount);
		assertThat(dto.createdAt()).isEqualTo(createdAt);
		assertThat(dto.price()).isEqualTo(price);
		assertThat(dto.cityId()).isEqualTo(mockCity.getCityId());
		assertThat(dto.subcategoryId()).isEqualTo(mockSubcategory.getId());
		assertThat(dto.currencyId()).isEqualTo(mockCurrency.getCurrencyId());
	}
}
