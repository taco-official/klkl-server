package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType {
	NORTHEAST_ASIA("동북아시아"),

	SOUTHEAST_ASIA("동남아시아"),

	ETC("기타"),

	NONE("");

	private final String koreanName;

	public static RegionType getRegionByName(String regionName) {
		return Arrays.stream(RegionType.values())
			.filter(r -> r.getKoreanName().equals(regionName))
			.findFirst()
			.orElse(NONE);
	}
}
