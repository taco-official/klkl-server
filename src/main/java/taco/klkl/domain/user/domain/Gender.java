package taco.klkl.domain.user.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
	MALE("남"),
	FEMALE("여"),
	NONE(""),
	;

	private final String description;

	public static Gender getGenderByDescription(String description) {
		return Arrays.stream(Gender.values())
			.filter(g -> g.getDescription().equals(description))
			.findFirst()
			.orElse(NONE);
	}

}
