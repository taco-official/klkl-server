package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.domain.City;

class ProductSimpleResponseDtoTest {
	@Test
	@DisplayName("Product 객체로부터 ProductSimpleResponseDto 생성 테스트")
	void testFromProduct() {
		// given
		Long productId = 1L;
		String name = "맛있는 곤약젤리";
		int likeCount = 10;
		City mockCity = mock(City.class);
		Subcategory mockSubcategory = mock(Subcategory.class);

		Product mockProduct = mock(Product.class);
		when(mockProduct.getProductId()).thenReturn(productId);
		when(mockProduct.getName()).thenReturn(name);
		when(mockProduct.getLikeCount()).thenReturn(likeCount);
		when(mockProduct.getCity()).thenReturn(mockCity);
		when(mockProduct.getSubcategory()).thenReturn(mockSubcategory);

		// when
		ProductSimpleResponseDto dto = ProductSimpleResponseDto.from(mockProduct);

		// then
		assertThat(dto.productId()).isEqualTo(productId);
		assertThat(dto.name()).isEqualTo(name);
		assertThat(dto.likeCount()).isEqualTo(likeCount);
		assertThat(dto.cityId()).isEqualTo(mockCity.getCityId());
		assertThat(dto.subcategoryId()).isEqualTo(mockSubcategory.getId());
	}

	@Test
	@DisplayName("생성자를 통해 ProductSimpleResponseDto 생성 테스트")
	void testConstructor() {
		// given
		Long productId = 1L;
		String name = "맛있는 곤약젤리";
		int likeCount = 10;
		City mockCity = mock(City.class);
		Subcategory mockSubcategory = mock(Subcategory.class);

		// when
		ProductSimpleResponseDto dto = new ProductSimpleResponseDto(
			productId,
			name,
			likeCount,
			mockCity.getCityId(),
			mockSubcategory.getId()
		);

		// then
		assertThat(dto.productId()).isEqualTo(productId);
		assertThat(dto.name()).isEqualTo(name);
		assertThat(dto.likeCount()).isEqualTo(likeCount);
		assertThat(dto.cityId()).isEqualTo(mockCity.getCityId());
		assertThat(dto.subcategoryId()).isEqualTo(mockSubcategory.getId());
	}
}
