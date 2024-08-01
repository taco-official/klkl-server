package taco.klkl.global.common.constants;

public final class ProductValidationMessages {

	public static final String NAME_NOT_NULL = "상품명은 필수 항목입니다.";
	public static final String NAME_NOT_BLANK = "상품명은 비어있을 수 없습니다.";
	public static final String NAME_SIZE = "상품명은 100자 이하여야 합니다.";

	public static final String DESCRIPTION_NOT_NULL = "상품 설명은 필수 항목입니다.";
	public static final String DESCRIPTION_NOT_BLANK = "상품 설명은 비어있을 수 없습니다.";
	public static final String DESCRIPTION_SIZE = "상품 설명은 2000자 이하여야 합니다.";

	public static final String ADDRESS_SIZE = "주소는 100자 이하여야 합니다.";

	public static final String PRICE_POSITIVE_OR_ZERO = "가격은 0 이상이어야 합니다.";

	public static final String CITY_ID_NOT_NULL = "도시 ID는 필수 항목입니다.";
	public static final String SUBCATEGORY_ID_NOT_NULL = "상품 소분류 ID는 필수 항목입니다.";
	public static final String CURRENCY_ID_NOT_NULL = "통화 ID는 필수 항목입니다.";

	private ProductValidationMessages() {
	}
}
