package taco.klkl.domain.search.dto.response;

import java.util.List;

import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;

public record SearchResponseDto(
	List<CountrySimpleResponseDto> countries,
	List<CityResponseDto> cities,
	List<ProductSimpleResponseDto> products
) {
	public static SearchResponseDto of(
		List<CountrySimpleResponseDto> countries,
		List<CityResponseDto> cities,
		List<ProductSimpleResponseDto> products
	) {
		return new SearchResponseDto(countries, cities, products);
	}
}
