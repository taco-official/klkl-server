package taco.klkl.domain.category.domain;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import taco.klkl.domain.category.exception.CategoryNameNotFoundException;

@Getter
@AllArgsConstructor
public enum CategoryName {
	FOOD("식품"),
	CLOTHES("의류"),
	SUNDRIES("잡화"),
	COSMETICS("화장품"),
	;

	private final String koreanName;

	public static CategoryName fromKoreanName(final String koreanName) {
		return Arrays.stream(values())
			.filter(categoryName -> categoryName.getKoreanName().equals(koreanName))
			.findFirst()
			.orElseThrow(CategoryNameNotFoundException::new);
	}

	public static List<CategoryName> findByPartialString(final String partialString) {

		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}

		String regex = ".*" + Pattern.quote(partialString) + ".*";
		Pattern pattern = Pattern.compile(regex);

		return Arrays.stream(CategoryName.values())
			.filter(c -> pattern.matcher(c.getKoreanName()).matches())
			.toList();
	}
}
