package taco.klkl.domain.category.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagName {

	CONVENIENCE_STORE("편의점"),
	CILANTRO("고수"),
	NONE(""),
	;

	private final String koreanName;

	public static TagName getByName(String name) {
		return Arrays.stream(values())
			.filter(tagName -> tagName.getKoreanName().equals(name))
			.findFirst()
			.orElse(NONE);
	}
}
