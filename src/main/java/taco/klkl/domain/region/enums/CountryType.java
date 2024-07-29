package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryType {
	// 일본
	JAPAN(392, "일본"),

	// 중국
	CHINA(156, "중국"),

	// 대만
	TAIWAN(158, "대만"),

	// 태국
	THAILAND(764, "태국"),

	// 베트남
	VIETNAM(704, "베트남"),

	// 필리핀
	PHILIPPINES(608, "필리핀"),

	// 싱가포르
	SINGAPORE(702, "싱가포르"),

	// 인도네시아
	INDONESIA(360, "인도네시아"),

	// 말레이시아
	MALAYSIA(458, "말레이시아"),

	// 괌
	GUAM(316, "괌"),

	// 미국
	USA(840, "미국"),

	// NONE
	NONE(0, "");

	private final long countryCode;
	private final String name;

	public static CountryType getCountryTypeByCode(int countryCode) {
		return Arrays.stream(CountryType.values())
			.filter(c -> c.getCountryCode() == countryCode)
			.findFirst()
			.orElse(NONE);
	}

	public static CountryType getCountryTypeByName(String name) {
		return Arrays.stream(CountryType.values())
			.filter(c -> c.getName().equals(name))
			.findFirst()
			.orElse(NONE);
	}
}
