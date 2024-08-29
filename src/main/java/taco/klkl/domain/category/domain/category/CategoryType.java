package taco.klkl.domain.category.domain.category;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.category.exception.category.CategoryTypeNotFoundException;

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
			.orElseThrow(CategoryTypeNotFoundException::new);
	}
}
