package taco.klkl.domain.category.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.category.exception.TagNameNotFoundException;

@Getter
@AllArgsConstructor
public enum TagName {

	CONVENIENCE_STORE("편의점"),
	CILANTRO("고수"),
	;

	private final String koreanName;

	public static TagName fromKoreanName(final String koreanName) {
		return Arrays.stream(values())
			.filter(tagName -> tagName.getKoreanName().equals(koreanName))
			.findFirst()
			.orElseThrow(TagNameNotFoundException::new);
	}
}
