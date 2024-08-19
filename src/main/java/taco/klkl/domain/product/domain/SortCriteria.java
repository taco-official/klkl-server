package taco.klkl.domain.product.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.product.exception.SortCriteriaNotFoundException;

@Getter
@RequiredArgsConstructor
public enum SortCriteria {
	LIKE_COUNT("like_count", "likeCount"),
	RATING("rating", "rating"),
	CREATED_AT("created_at", "createdAt"),
	;

	private final String query;
	private final String value;

	public static SortCriteria fromQuery(final String query) {
		return Arrays.stream(SortCriteria.values())
			.filter(s -> s.query.equals(query))
			.findFirst()
			.orElseThrow(SortCriteriaNotFoundException::new);
	}
}
