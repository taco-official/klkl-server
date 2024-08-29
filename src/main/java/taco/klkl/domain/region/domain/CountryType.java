package taco.klkl.domain.region.domain;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.region.exception.CountryTypeNotFoundException;

@Getter
@AllArgsConstructor
public enum CountryType {
	JAPAN("일본", "JP"),

	CHINA("중국", "CN"),

	TAIWAN("대만", "TW"),

	THAILAND("태국", "TH"),

	VIETNAM("베트남", "VN"),

	PHILIPPINES("필리핀", "PH"),

	SINGAPORE("싱가포르", "SG"),

	INDONESIA("인도네시아", "ID"),

	MALAYSIA("말레이시아", "MY"),

	GUAM("괌", "GU"),

	USA("미국", "US"),
	;

	private final String koreanName;
	private final String code;

	/**
	 *
	 * @param koreanName CountryType 이름
	 * @return CountryType
	 */
	public static CountryType getCountryTypeByName(String koreanName) {
		return Arrays.stream(CountryType.values())
			.filter(c -> c.getKoreanName().equals(koreanName))
			.findFirst()
			.orElseThrow(CountryTypeNotFoundException::new);
	}

	/**
	 * 부분문자열을 포함하는 CountryType을 찾는 함수
	 * @param partialString 부분 문자열
	 * @return 부분문자열을 포함하는 CountryType의 리스트
	 */
	public static List<CountryType> getCountryTypesByPartialString(String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		String regex = ".*" + Pattern.quote(partialString) + ".*";
		Pattern pattern = Pattern.compile(regex);
		return Arrays.stream(CountryType.values())
			.filter(c -> pattern.matcher(c.getKoreanName()).matches())
			.toList();
	}
}
