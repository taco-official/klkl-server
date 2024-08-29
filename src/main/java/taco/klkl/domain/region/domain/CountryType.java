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
	JAPAN("JP", "일본"),

	CHINA("CN", "중국"),

	TAIWAN("TW", "대만"),

	THAILAND("TH", "태국"),

	VIETNAM("VN", "베트남"),

	PHILIPPINES("PH", "필리핀"),

	SINGAPORE("SG", "싱가포르"),

	INDONESIA("ID", "인도네시아"),

	MALAYSIA("MY", "말레이시아"),

	GUAM("GU", "괌"),

	USA("US", "미국"),
	;

	private final String code;
	private final String name;

	/**
	 *
	 * @param name CountryType 이름
	 * @return CountryType
	 */
	public static CountryType from(final String name) {
		return Arrays.stream(CountryType.values())
			.filter(c -> c.getName().equals(name))
			.findFirst()
			.orElseThrow(CountryTypeNotFoundException::new);
	}

	/**
	 * 부분문자열을 포함하는 CountryType을 찾는 함수
	 * @param partialString 부분 문자열
	 * @return 부분문자열을 포함하는 CountryType의 리스트
	 */
	public static List<CountryType> findCountryTypesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		String regex = ".*" + Pattern.quote(partialString) + ".*";
		Pattern pattern = Pattern.compile(regex);
		return Arrays.stream(CountryType.values())
			.filter(c -> pattern.matcher(c.getName()).matches())
			.toList();
	}
}
