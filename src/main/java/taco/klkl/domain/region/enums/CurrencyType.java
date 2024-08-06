package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyType {
	JAPANESE_YEN("JPY"),

	CHINESE_YUAN("CNY"),

	NEW_TAIWAN_DOLLAR("TWD"),

	THAI_BAHT("THB"),

	VIETNAMESE_DONG("VND"),

	PHILIPPINE_PESO("PHP"),

	SINGAPORE_DOLLAR("SGD"),

	INDONESIAN_RUPIAH("IDR"),

	MALAYSIAN_RINGGIT("MYR"),

	UNITED_STATES_DOLLAR("USD"),

	NONE("");

	private final String codeName;

	/**
	 *
	 * @param codeName CurrencyType code
	 * @return CurrencyType
	 */
	public static CurrencyType getCurrencyTypeByCode(String codeName) {
		return Arrays.stream(CurrencyType.values())
			.filter(c -> c.getCodeName().equals(codeName))
			.findFirst()
			.orElse(NONE);
	}
}
