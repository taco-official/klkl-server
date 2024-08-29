package taco.klkl.domain.region.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.region.exception.CurrencyTypeNotFoundException;

@Getter
@AllArgsConstructor
public enum CurrencyType {
	JAPANESE_YEN("JPY", "엔"),

	CHINESE_YUAN("CNH", "위안"),

	NEW_TAIWAN_DOLLAR("TWD", "달러"),

	THAI_BAHT("THB", "바트"),

	VIETNAMESE_DONG("VND", "동"),

	PHILIPPINE_PESO("PHP", "페"),

	SINGAPORE_DOLLAR("SGD", "달러"),

	INDONESIAN_RUPIAH("IDR", "루피아"),

	MALAYSIAN_RINGGIT("MYR", "링깃"),

	UNITED_STATES_DOLLAR("USD", "달러"),
	;

	private final String code;
	private final String koreanUnit;

	/**
	 *
	 * @param codeName CurrencyType code
	 * @return CurrencyType
	 */
	public static CurrencyType getCurrencyTypeByCodeName(String codeName) {
		return Arrays.stream(CurrencyType.values())
			.filter(c -> c.getCode().equals(codeName))
			.findFirst()
			.orElseThrow(CurrencyTypeNotFoundException::new);
	}
}
