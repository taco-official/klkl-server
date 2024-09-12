package taco.klkl.domain.region.dto.response.country;

import taco.klkl.domain.region.domain.country.Country;

/**
 *
 * @param id
 * @param name
 * @param wallpaper
 */
public record CountryResponse(
	Long id,
	String name,
	String wallpaper
) {
	/**
	 *
	 * @param country
	 * @return CountryResponse
	 */
	public static CountryResponse from(final Country country) {
		return new CountryResponse(
			country.getId(),
			country.getName(),
			country.getWallpaper()
		);
	}
}
