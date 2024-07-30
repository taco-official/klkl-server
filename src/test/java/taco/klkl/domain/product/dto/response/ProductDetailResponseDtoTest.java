package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

class ProductDetailResponseDtoTest {
	@Test
	@DisplayName("Product 객체로부터 ProductDetailResponseDto 생성 테스트")
	void testFromProduct() {
		// given
		Long productId = 1L;
		Long userId = 2L;
		String name = "맛있는 곤약젤리";
		String description = "탱글탱글 맛있는 곤약젤리";
		String address = "신사이바시 메가돈키호테";
		int likeCount = 10;
		LocalDateTime createdAt = LocalDateTime.now();
		int price = 1000;
		Long cityId = 3L;
		Long subcategoryId = 4L;
		Long currencyId = 5L;

		User mockUser = Mockito.mock(User.class);
		when(mockUser.getId()).thenReturn(userId);

		Product product = Mockito.mock(Product.class);
		when(product.getProductId()).thenReturn(productId);
		when(product.getUser()).thenReturn(mockUser);
		when(product.getName()).thenReturn(name);
		when(product.getDescription()).thenReturn(description);
		when(product.getAddress()).thenReturn(address);
		when(product.getLikeCount()).thenReturn(likeCount);
		when(product.getCreatedAt()).thenReturn(createdAt);
		when(product.getPrice()).thenReturn(price);
		when(product.getCityId()).thenReturn(cityId);
		when(product.getSubcategoryId()).thenReturn(subcategoryId);
		when(product.getCurrencyId()).thenReturn(currencyId);

		// when
		ProductDetailResponseDto dto = ProductDetailResponseDto.from(product);

		// then
		assertThat(dto.productId()).isEqualTo(productId);
		assertThat(dto.userId()).isEqualTo(userId);
		assertThat(dto.name()).isEqualTo(name);
		assertThat(dto.description()).isEqualTo(description);
		assertThat(dto.address()).isEqualTo(address);
		assertThat(dto.likeCount()).isEqualTo(likeCount);
		assertThat(dto.createdAt()).isEqualTo(createdAt);
		assertThat(dto.price()).isEqualTo(price);
		assertThat(dto.cityId()).isEqualTo(cityId);
		assertThat(dto.subcategoryId()).isEqualTo(subcategoryId);
		assertThat(dto.currencyId()).isEqualTo(currencyId);
	}

	@Test
	@DisplayName("ProductDetailResponseDto 생성자 테스트")
	void testConstructor() {
		// given
		Long productId = 1L;
		Long userId = 2L;
		String name = "맛있는 곤약젤리";
		String description = "탱글탱글 맛있는 곤약젤리";
		String address = "신사이바시 메가돈키호테";
		int likeCount = 10;
		LocalDateTime createdAt = LocalDateTime.now();
		int price = 1000;
		Long cityId = 3L;
		Long subcategoryId = 4L;
		Long currencyId = 5L;

		// when
		ProductDetailResponseDto dto = new ProductDetailResponseDto(
			productId,
			userId,
			name,
			description,
			address,
			likeCount,
			createdAt,
			price,
			cityId,
			subcategoryId,
			currencyId
		);

		// then
		assertThat(dto.productId()).isEqualTo(productId);
		assertThat(dto.userId()).isEqualTo(userId);
		assertThat(dto.name()).isEqualTo(name);
		assertThat(dto.description()).isEqualTo(description);
		assertThat(dto.address()).isEqualTo(address);
		assertThat(dto.likeCount()).isEqualTo(likeCount);
		assertThat(dto.createdAt()).isEqualTo(createdAt);
		assertThat(dto.price()).isEqualTo(price);
		assertThat(dto.cityId()).isEqualTo(cityId);
		assertThat(dto.subcategoryId()).isEqualTo(subcategoryId);
		assertThat(dto.currencyId()).isEqualTo(currencyId);
	}
}
