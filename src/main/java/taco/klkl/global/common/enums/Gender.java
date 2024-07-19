package taco.klkl.global.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
	MALE("남"),
	FEMALE("여");

	private final String description;
}
