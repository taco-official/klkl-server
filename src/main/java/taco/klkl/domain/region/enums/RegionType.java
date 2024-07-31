package taco.klkl.domain.region.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType {
	// 동북아시아
	NORTHEAST_ASIA("동북아시아"),

	// 동남아시아
	SOUTHEAST_ASIA("동남아시아"),

	// 기타
	ETC_REGION("기타"),

	// NONE
	NONE("");

	private final String name;

	public static RegionType getRegionByName(String regionName) {
		return Arrays.stream(RegionType.values())
			.filter(r -> r.getName().equals(regionName))
			.findFirst()
			.orElse(NONE);
	}
}
