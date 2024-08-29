package taco.klkl.domain.region.domain;

public final class FlagUrlGenerator {
	private static final String FLAG_URL_DOMAIN = "https://flagcdn.com";

	public static String generateSvgUrl(final String countryCode) {
		return FLAG_URL_DOMAIN + "/" + countryCode.toLowerCase() + ".svg";
	}
}
