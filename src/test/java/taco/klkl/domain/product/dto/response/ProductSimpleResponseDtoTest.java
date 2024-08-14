package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.FilterName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductFilter;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.enums.CurrencyType;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.user.domain.User;

class ProductSimpleResponseDtoTest {

	private Product product;
	private User mockUser;
	private City city;
	private Subcategory subcategory;
	private Currency currency;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(1L);
		when(mockUser.getName()).thenReturn("Test User");

		Region region = Region.of(RegionType.SOUTHEAST_ASIA);
		currency = Currency.of(
			CurrencyType.THAI_BAHT,
			"image/baht.jpg"
		);
		Country country = Country.of(
			CountryType.JAPAN,
			region,
			"image/thailand-flag.jpg",
			"image/thailand-photo.jpg",
			currency
		);
		city = City.of(
			country,
			CityType.BANGKOK
		);

		Category category = Category.of(CategoryName.FOOD);
		subcategory = Subcategory.of(
			category,
			SubcategoryName.INSTANT_FOOD
		);

		Filter filter1 = Filter.of(FilterName.CILANTRO);
		Filter filter2 = Filter.of(FilterName.CONVENIENCE_STORE);
		Set<ProductFilter> productFilters = Set.of(filter1, filter2).stream()
			.map(filter -> ProductFilter.of(product, filter))
			.collect(Collectors.toSet());

		product = mock(Product.class);
		when(product.getId()).thenReturn(1L);
		when(product.getName()).thenReturn("productName");
		when(product.getDescription()).thenReturn("productDescription");
		when(product.getAddress()).thenReturn("productAddress");
		when(product.getPrice()).thenReturn(1000);
		when(product.getLikeCount()).thenReturn(0);
		when(product.getUser()).thenReturn(mockUser);
		when(product.getCity()).thenReturn(city);
		when(product.getSubcategory()).thenReturn(subcategory);
		when(product.getCurrency()).thenReturn(currency);
		when(product.getFilters()).thenReturn(productFilters);
	}

	@Test
	@DisplayName("Product 객체로부터 ProductSimpleResponseDto 생성 테스트")
	void testFromProduct() {
		// when
		ProductSimpleResponseDto dto = ProductSimpleResponseDto.from(product);

		// then
		assertThat(dto.id()).isEqualTo(product.getId());
		assertThat(dto.name()).isEqualTo(product.getName());
		assertThat(dto.likeCount()).isEqualTo(product.getLikeCount());
		assertThat(dto.countryName()).isEqualTo(product.getCity().getCountry().getName().getKoreanName());
		assertThat(dto.categoryName()).isEqualTo(product.getSubcategory().getCategory().getName().getKoreanName());
	}

	@Test
	@DisplayName("생성자를 통해 ProductSimpleResponseDto 생성 테스트")
	void testConstructor() {
		// when
		Set<FilterResponseDto> filters = product.getFilters().stream()
			.map(ProductFilter::getFilter)
			.map(FilterResponseDto::from)
			.collect(Collectors.toSet());

		ProductSimpleResponseDto dto = new ProductSimpleResponseDto(
			product.getId(),
			product.getName(),
			product.getLikeCount(),
			city.getCountry().getName().getKoreanName(),
			product.getSubcategory().getCategory().getName().getKoreanName(),
			filters
		);

		// then
		assertThat(dto.id()).isEqualTo(product.getId());
		assertThat(dto.name()).isEqualTo(product.getName());
		assertThat(dto.likeCount()).isEqualTo(product.getLikeCount());
		assertThat(dto.countryName()).isEqualTo(city.getCountry().getName().getKoreanName());
		assertThat(dto.categoryName()).isEqualTo(product.getSubcategory().getCategory().getName().getKoreanName());
	}
}
