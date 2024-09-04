package taco.klkl.domain.region.dto.response.country;

import java.util.List;

import taco.klkl.domain.region.domain.FlagUrlGenerator;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;

/**
 *
 * @param id
 * @param name
 * @param flagUrl
 * @param photo
 * @param currency
 * @param cities
 */
public record CountryResponse(
	Long id,
	String name,
	String flagUrl,
	String photo,
	CurrencyResponse currency,
	List<CityResponse> cities
) {
	/**
	 *
	 * @param country
	 * @return CountryResponse
	 */
	public static CountryResponse from(final Country country) {
		final List<CityResponse> cities = country.getCities().stream()
			.map(CityResponse::from)
			.toList();

		return new CountryResponse(
			country.getId(),
			country.getName(),
			FlagUrlGenerator.generateSvgUrlByCountryCode(country.getCode()),
			country.getPhoto(),
			CurrencyResponse.from(country.getCurrency()),
			cities
		);
	}

}
