package taco.klkl.domain.category.domain.tag;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.category.exception.tag.TagTypeNotFoundException;

@Getter
@AllArgsConstructor
public enum TagType {
	CONVENIENCE_STORE("편의점"),
	CILANTRO("고수"),
	;

	private final String name;

	public static TagType from(final String name) {
		return Arrays.stream(values())
			.filter(type -> type.getName().equals(name))
			.findFirst()
			.orElseThrow(TagTypeNotFoundException::new);
	}
}
