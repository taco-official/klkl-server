package taco.klkl.domain.product.dto.response;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.domain.tag.Tag;
import taco.klkl.domain.category.domain.tag.TagType;
import taco.klkl.domain.category.dto.response.tag.TagSimpleResponse;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductTag;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.global.util.ProductUtil;

class ProductSimpleResponseTest {

	private Product product;
	private Member mockMember;
	private City city;
	private Subcategory subcategory;
	private Currency currency;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		mockMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(mockMember.getName()).thenReturn("Test Member");

		Region region = Region.from(RegionType.SOUTHEAST_ASIA);
		currency = Currency.of(
			CurrencyType.THAI_BAHT
		);
		Country country = Country.of(
			CountryType.JAPAN,
			region,
			"image/thailand-wallpaper.jpg",
			currency
		);
		city = City.of(
			CityType.BANGKOK,
			country
		);

		Category category = Category.of(CategoryType.FOOD);
		subcategory = Subcategory.of(
			category,
			SubcategoryType.INSTANT_FOOD
		);

		Tag tag1 = Tag.of(TagType.CILANTRO);
		Tag tag2 = Tag.of(TagType.CONVENIENCE_STORE);
		Set<ProductTag> productTags = Set.of(tag1, tag2).stream()
			.map(tag -> ProductTag.of(product, tag))
			.collect(Collectors.toSet());

		product = mock(Product.class);
		when(product.getId()).thenReturn(1L);
		when(product.getName()).thenReturn("productName");
		when(product.getDescription()).thenReturn("productDescription");
		when(product.getAddress()).thenReturn("productAddress");
		when(product.getPrice()).thenReturn(1000);
		when(product.getLikeCount()).thenReturn(0);
		when(product.getRating()).thenReturn(Rating.FIVE);
		when(product.getMember()).thenReturn(mockMember);
		when(product.getCity()).thenReturn(city);
		when(product.getSubcategory()).thenReturn(subcategory);
		when(product.getCurrency()).thenReturn(currency);
		when(product.getProductTags()).thenReturn(productTags);
	}

	@Test
	@DisplayName("Product 객체로부터 ProductSimpleResponse 생성 테스트")
	void testFromProduct() {
		// when
		ProductSimpleResponse dto = ProductSimpleResponse.from(product, false);

		// then
		assertThat(dto.id()).isEqualTo(product.getId());
		assertThat(dto.name()).isEqualTo(product.getName());
		assertThat(dto.likeCount()).isEqualTo(product.getLikeCount());
		assertThat(dto.rating()).isEqualTo(product.getRating().getValue());
		assertThat(dto.countryName()).isEqualTo(product.getCity().getCountry().getName());
		assertThat(dto.categoryName()).isEqualTo(product.getSubcategory().getCategory().getName());
	}

	@Test
	@DisplayName("생성자를 통해 ProductSimpleResponse 생성 테스트")
	void testConstructor() {
		// given
		Set<TagSimpleResponse> tags = ProductUtil.generateTagsByProduct(product);

		// when
		ProductSimpleResponse dto = new ProductSimpleResponse(
			product.getId(),
			ImageResponse.from(product.getMainImage()),
			product.getName(),
			product.getLikeCount(),
			product.getRating().getValue(),
			product.getCity().getCountry().getName(),
			product.getSubcategory().getCategory().getName(),
			tags,
			false
		);

		// then
		assertThat(dto.id()).isEqualTo(product.getId());
		assertThat(dto.name()).isEqualTo(product.getName());
		assertThat(dto.likeCount()).isEqualTo(product.getLikeCount());
		assertThat(dto.rating()).isEqualTo(product.getRating().getValue());
		assertThat(dto.countryName()).isEqualTo(product.getCity().getCountry().getName());
		assertThat(dto.categoryName()).isEqualTo(product.getSubcategory().getCategory().getName());
		assertThat(dto.tags()).isEqualTo(tags);
	}
}
