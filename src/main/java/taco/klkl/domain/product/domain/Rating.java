package taco.klkl.domain.product.domain;

import java.util.Arrays;

import lombok.Getter;
import taco.klkl.domain.product.exception.RatingNotFoundException;

@Getter
public enum Rating {
	ZERO_FIVE(0.5),
	ONE(1.0),
	ONE_FIVE(1.5),
	TWO(2.0),
	TWO_FIVE(2.5),
	THREE(3.0),
	THREE_FIVE(3.5),
	FOUR(4.0),
	FOUR_FIVE(4.5),
	FIVE(5.0),
	;

	private final double value;

	Rating(final double value) {
		this.value = value;
	}

	public static Rating from(final double value) {
		return Arrays.stream(Rating.values())
			.filter(rating -> rating.value == value)
			.findFirst()
			.orElseThrow(RatingNotFoundException::new);
	}
}
