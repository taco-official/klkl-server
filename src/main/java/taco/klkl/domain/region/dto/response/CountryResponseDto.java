package taco.klkl.domain.region.dto.response;

import java.util.Objects;

import taco.klkl.domain.region.domain.Country;

public record CountryResponseDto(
	Long countryId,
	RegionSimpleResponseDto region,
	String name,
	String flag,
	String photo,
	int currencyId
) {
	/**
	 *
	 * @param country
	 * @return CountryResponseDto
	 */
	public static CountryResponseDto from(Country country) {
		return new CountryResponseDto(
			country.getCountryId(),
			RegionSimpleResponseDto.from(country.getRegion()),
			country.getName().getKoreanName(),
			country.getFlag(),
			country.getPhoto(),
			country.getCurrencyId());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		CountryResponseDto that = (CountryResponseDto)object;
		return currencyId == that.currencyId
			&& Objects.equals(name, that.name)
			&& Objects.equals(flag, that.flag)
			&& Objects.equals(photo, that.photo)
			&& Objects.equals(countryId, that.countryId)
			&& Objects.equals(region, that.region);
	}

	@Override
	public int hashCode() {
		return Objects.hash(countryId, region, name, flag, photo, currencyId);
	}
}
