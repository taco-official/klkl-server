package taco.klkl.domain.category.domain;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.category.exception.CategoryNameNotFoundException;

@Getter
@AllArgsConstructor
public enum CategoryType {
	FOOD("식품"),
	CLOTHES("의류"),
	SUNDRIES("잡화"),
	COSMETICS("화장품"),
	;

	private final String name;

	public static CategoryType from(final String name) {
		return Arrays.stream(values())
			.filter(type -> type.getName().equals(name))
			.findFirst()
			.orElseThrow(CategoryNameNotFoundException::new);
	}
}
