package taco.klkl.domain.region.domain.country;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.region.exception.country.CountryTypeNotFoundException;

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
}
