package taco.klkl.domain.region.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.region.exception.CurrencyTypeNotFoundException;

@Getter
@AllArgsConstructor
public enum CurrencyType {
	JAPANESE_YEN("JPY"),

	CHINESE_YUAN("CNH"),

	NEW_TAIWAN_DOLLAR("TWD"),

	THAI_BAHT("THB"),

	VIETNAMESE_DONG("VND"),

	PHILIPPINE_PESO("PHP"),

	SINGAPORE_DOLLAR("SGD"),

	INDONESIAN_RUPIAH("IDR"),

	MALAYSIAN_RINGGIT("MYR"),

	UNITED_STATES_DOLLAR("USD"),
	;

	private final String codeName;

	/**
	 *
	 * @param codeName CurrencyType code
	 * @return CurrencyType
	 */
	public static CurrencyType getCurrencyTypeByCodeName(String codeName) {
		return Arrays.stream(CurrencyType.values())
			.filter(c -> c.getCodeName().equals(codeName))
			.findFirst()
			.orElseThrow(CurrencyTypeNotFoundException::new);
	}
}
