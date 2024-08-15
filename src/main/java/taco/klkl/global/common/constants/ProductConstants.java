package taco.klkl.global.common.constants;

import java.util.Set;

public final class ProductConstants {

	public static final int DEFAULT_PAGE_SIZE = 9;

	public static final int DEFAULT_PRICE = 0;
	public static final int DEFAULT_LIKE_COUNT = 0;
	public static final String DEFAULT_ADDRESS = "N/A";

	public static final int NAME_MAX_LENGTH = 100;
	public static final int DESCRIPTION_MAX_LENGTH = 2000;
	public static final int ADDRESS_MAX_LENGTH = 100;

	public static final String RATING_MIN_VALUE = "0.5";
	public static final String RATING_MAX_VALUE = "5.0";

	public static final Set<String> ALLOWED_SORT_BY = Set.of("likeCount", "rating", "createdAt");
	public static final Set<String> ALLOWED_SORT_DIRECTION = Set.of("ASC", "DESC");

	private ProductConstants() {
	}
}
