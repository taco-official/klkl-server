package taco.klkl.domain.category.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryName {
	FOOD("식품"),
	CLOTHES("의류"),
	SUNDRIES("잡화"),
	COSMETICS("화장품"),
	NONE("");

	private final String koreanName;

	public static CategoryName getByName(String name) {
		return Arrays.stream(values())
			.filter(categoryName -> categoryName.getKoreanName().equals(name))
			.findFirst()
			.orElse(NONE);
	}
}
