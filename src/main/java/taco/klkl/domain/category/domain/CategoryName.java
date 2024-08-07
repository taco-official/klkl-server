package taco.klkl.domain.category.domain;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

	public static List<CategoryName> getCategoryNamesByPartialString(String partial) {

		if (partial == null || partial.isEmpty()) {
			return List.of();
		}

		String regex = ".*" + Pattern.quote(partial) + ".*";
		Pattern pattern = Pattern.compile(regex);

		return Arrays.stream(CategoryName.values())
			.filter(c -> pattern.matcher(c.getKoreanName()).matches())
			.toList();
	}
}
