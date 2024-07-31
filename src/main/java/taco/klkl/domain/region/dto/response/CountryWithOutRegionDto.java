package taco.klkl.domain.region.dto.response;

import java.util.Objects;

import taco.klkl.domain.region.domain.Country;

public record CountryWithOutRegionDto(
	Long countryId,
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
	public static CountryWithOutRegionDto from(Country country) {
		return new CountryWithOutRegionDto(
			country.getCountryId(),
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
		CountryWithOutRegionDto that = (CountryWithOutRegionDto)object;
		return currencyId == that.currencyId && Objects.equals(name, that.name) && Objects.equals(flag,
			that.flag) && Objects.equals(photo, that.photo) && Objects.equals(countryId,
			that.countryId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(countryId, name, flag, photo, currencyId);
	}
}
