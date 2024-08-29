package taco.klkl.domain.region.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.region.exception.RegionTypeNotFoundException;

@Getter
@AllArgsConstructor
public enum RegionType {
	NORTHEAST_ASIA("동북아시아"),

	SOUTHEAST_ASIA("동남아시아"),

	ETC("기타"),
	;

	private final String name;

	/**
	 *
	 * @param name RegionType koreanName
	 * @return RegionType
	 */
	public static RegionType from(final String name) {
		return Arrays.stream(RegionType.values())
			.filter(r -> r.getName().equals(name))
			.findFirst()
			.orElseThrow(RegionTypeNotFoundException::new);
	}
}
