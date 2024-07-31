package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryType {
	// 일본
	JAPAN("일본"),

	// 중국
	CHINA("중국"),

	// 대만
	TAIWAN("대만"),

	// 태국
	THAILAND("태국"),

	// 베트남
	VIETNAM("베트남"),

	// 필리핀
	PHILIPPINES("필리핀"),

	// 싱가포르
	SINGAPORE("싱가포르"),

	// 인도네시아
	INDONESIA("인도네시아"),

	// 말레이시아
	MALAYSIA("말레이시아"),

	// 괌
	GUAM("괌"),

	// 미국
	USA("미국"),

	// NONE
	NONE("");

	private final String name;

	public static CountryType getCountryTypeByName(String name) {
		return Arrays.stream(CountryType.values())
			.filter(c -> c.getName().equals(name))
			.findFirst()
			.orElse(NONE);
	}
}
