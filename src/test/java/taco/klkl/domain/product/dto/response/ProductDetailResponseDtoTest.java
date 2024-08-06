package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CurrencyResponseDto;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CurrencyType;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;

class ProductDetailResponseDtoTest {

	private Product mockProduct;
	private User mockUser;
	private City mockCity;
	private Subcategory mockSubcategory;
	private Currency mockCurrency;

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
	}

	@Test
	@DisplayName("Product 객체로부터 ProductDetailResponseDto 생성 테스트")
	void testFromProduct() {
		// when
		ProductDetailResponseDto dto = ProductDetailResponseDto.from(mockProduct);

		// then
		assertThat(dto.productId()).isEqualTo(mockProduct.getId());
		assertThat(dto.name()).isEqualTo(mockProduct.getName());
		assertThat(dto.description()).isEqualTo(mockProduct.getDescription());
		assertThat(dto.address()).isEqualTo(mockProduct.getAddress());
		assertThat(dto.price()).isEqualTo(mockProduct.getPrice());
		assertThat(dto.likeCount()).isEqualTo(mockProduct.getLikeCount());
		assertThat(dto.user()).isEqualTo(UserDetailResponseDto.from(mockUser));
		assertThat(dto.city()).isEqualTo(CityResponseDto.from(mockCity));
		assertThat(dto.subcategory()).isEqualTo(SubcategoryResponseDto.from(mockSubcategory));
		assertThat(dto.currency()).isEqualTo(CurrencyResponseDto.from(mockCurrency));
	}

	@Test
	@DisplayName("ProductDetailResponseDto 생성자 테스트")
	void testConstructor() {
		// when
		ProductDetailResponseDto dto = ProductDetailResponseDto.from(mockProduct);

		// then
		assertThat(dto.productId()).isEqualTo(mockProduct.getId());
		assertThat(dto.name()).isEqualTo(mockProduct.getName());
		assertThat(dto.description()).isEqualTo(mockProduct.getDescription());
		assertThat(dto.address()).isEqualTo(mockProduct.getAddress());
		assertThat(dto.price()).isEqualTo(mockProduct.getPrice());
		assertThat(dto.likeCount()).isEqualTo(mockProduct.getLikeCount());
		assertThat(dto.user()).isEqualTo(UserDetailResponseDto.from(mockUser));
		assertThat(dto.city()).isEqualTo(CityResponseDto.from(mockCity));
		assertThat(dto.subcategory()).isEqualTo(SubcategoryResponseDto.from(mockSubcategory));
		assertThat(dto.currency()).isEqualTo(CurrencyResponseDto.from(mockCurrency));
	}
}
