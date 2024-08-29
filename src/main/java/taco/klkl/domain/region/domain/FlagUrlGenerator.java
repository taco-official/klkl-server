package taco.klkl.domain.region.domain;

public final class FlagUrlGenerator {
	private static final String FLAG_URL_DOMAIN = "https://flagcdn.com";

	public static String generateSvgUrlByCountryCode(final String countryCode) {
		return FLAG_URL_DOMAIN + "/" + countryCode.toLowerCase() + ".svg";
	}

	public static String generateSvgUrlByCurrencyCode(final String currencyCode) {
		final String countryCode = currencyCode.substring(0, 2);
		return generateSvgUrlByCountryCode(countryCode);
	}
}
