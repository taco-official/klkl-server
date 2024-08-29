package taco.klkl.domain.user.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.user.exception.GenderNotFoundException;

@Getter
@AllArgsConstructor
public enum Gender {
	MALE("남"),
	FEMALE("여"),
	;

	private final String value;

	public static Gender from(final String value) {
		return Arrays.stream(Gender.values())
			.filter(gender -> gender.getValue().equals(value))
			.findFirst()
			.orElseThrow(GenderNotFoundException::new);
	}
}
