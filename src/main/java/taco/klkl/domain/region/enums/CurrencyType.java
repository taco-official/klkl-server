package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyType {
	YEN("JPY"),

	YUAN("CNY"),

	NEW_TAIWAN_DOLLAR("TWD"),

	BAHT("THB"),

	DONG("VND"),

	PESO("PHP"),

	SINGAPORE_DOLLAR("SGD"),

	RUPIAH("IDR"),

	RINGGIT("MYR"),

	US_DOLLAR("USD"),

	NONE("");

	private final String code;

	public static CurrencyType getCurrencyTypeByCode(String code) {
		return Arrays.stream(CurrencyType.values())
			.filter(c -> c.getCode().equals(code))
			.findFirst()
			.orElse(NONE);
	}
}
