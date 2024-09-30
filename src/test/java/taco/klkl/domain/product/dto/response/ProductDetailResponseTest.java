package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;

class ProductDetailResponseTest {

	private Product mockProduct;
	private Member mockMember;
	private City mockCity;
	private Subcategory mockSubcategory;
	private Currency mockCurrency;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(mockMember.getName()).thenReturn("Test Member");

		mockCity = mock(City.class);
		when(mockCity.getId()).thenReturn(1L);
		when(mockCity.getName()).thenReturn(CityType.BANGKOK.getName());

		mockSubcategory = mock(Subcategory.class);
		when(mockSubcategory.getId()).thenReturn(1L);
		when(mockSubcategory.getName()).thenReturn(SubcategoryType.INSTANT_FOOD.getName());

		mockCurrency = mock(Currency.class);
		when(mockCurrency.getId()).thenReturn(1L);
		when(mockCurrency.getCode()).thenReturn(CurrencyType.THAI_BAHT.getCode());

		mockProduct = mock(Product.class);
		when(mockProduct.getId()).thenReturn(1L);
		when(mockProduct.getName()).thenReturn("productName");
		when(mockProduct.getDescription()).thenReturn("productDescription");
		when(mockProduct.getAddress()).thenReturn("productAddress");
		when(mockProduct.getPrice()).thenReturn(1000);
		when(mockProduct.getLikeCount()).thenReturn(0);
		when(mockProduct.getRating()).thenReturn(Rating.FIVE);
		when(mockProduct.getMember()).thenReturn(mockMember);
		when(mockProduct.getCity()).thenReturn(mockCity);
		when(mockProduct.getSubcategory()).thenReturn(mockSubcategory);
		when(mockProduct.getCurrency()).thenReturn(mockCurrency);
	}

	@Test
	@DisplayName("Product 객체로부터 ProductDetailResponse 생성 테스트")
	void testFromProduct() {
		// when
		ProductDetailResponse dto = ProductDetailResponse.from(mockProduct);

		// then
		assertThat(dto.id()).isEqualTo(mockProduct.getId());
		assertThat(dto.name()).isEqualTo(mockProduct.getName());
		assertThat(dto.description()).isEqualTo(mockProduct.getDescription());
		assertThat(dto.address()).isEqualTo(mockProduct.getAddress());
		assertThat(dto.price()).isEqualTo(mockProduct.getPrice());
		assertThat(dto.likeCount()).isEqualTo(mockProduct.getLikeCount());
		assertThat(dto.rating()).isEqualTo(mockProduct.getRating().getValue());
		assertThat(dto.member()).isEqualTo(MemberDetailResponse.from(mockMember));
		assertThat(dto.city()).isEqualTo(CitySimpleResponse.from(mockCity));
		assertThat(dto.subcategory()).isEqualTo(SubcategorySimpleResponse.from(mockSubcategory));
		assertThat(dto.currency()).isEqualTo(CurrencyResponse.from(mockCurrency));
	}

	@Test
	@DisplayName("ProductDetailResponse 생성자 테스트")
	void testConstructor() {
		// when
		ProductDetailResponse dto = ProductDetailResponse.from(mockProduct);

		// then
		assertThat(dto.id()).isEqualTo(mockProduct.getId());
		assertThat(dto.name()).isEqualTo(mockProduct.getName());
		assertThat(dto.description()).isEqualTo(mockProduct.getDescription());
		assertThat(dto.address()).isEqualTo(mockProduct.getAddress());
		assertThat(dto.price()).isEqualTo(mockProduct.getPrice());
		assertThat(dto.likeCount()).isEqualTo(mockProduct.getLikeCount());
		assertThat(dto.rating()).isEqualTo(mockProduct.getRating().getValue());
		assertThat(dto.member()).isEqualTo(MemberDetailResponse.from(mockMember));
		assertThat(dto.city()).isEqualTo(CitySimpleResponse.from(mockCity));
		assertThat(dto.subcategory()).isEqualTo(SubcategorySimpleResponse.from(mockSubcategory));
		assertThat(dto.currency()).isEqualTo(CurrencyResponse.from(mockCurrency));
	}
}
