package taco.klkl.global.common.constants;

import taco.klkl.domain.product.domain.Product;

public final class ProductConstants {

	public static final String DEFAULT_PAGE_NUMBER = "0";
	public static final String DEFAULT_PAGE_SIZE = "9";

	public static final int DEFAULT_PRICE = 0;
	public static final int DEFAULT_LIKE_COUNT = 0;
	public static final String DEFAULT_ADDRESS = "N/A";

	public static final int NAME_MAX_LENGTH = 100;
	public static final int DESCRIPTION_MAX_LENGTH = 2000;
	public static final int ADDRESS_MAX_LENGTH = 100;

	public static final Product TEST_PRODUCT = Product.of(
		UserConstants.TEST_USER,
		"testProduct",
		"testDescription",
		"testAddress",
		1000,
		1L,
		2L,
		3L
	);
	public static final Product TEST_PRODUCT_TWO = Product.of(
		UserConstants.TEST_USER,
		"testProductTwo",
		"testDescriptionTwo",
		"testAddressTwo",
		2000,
		2L,
		4L,
		6L
	);

	private ProductConstants() {
	}
}
