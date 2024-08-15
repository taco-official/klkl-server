package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class RatingNotFoundException extends CustomException {
	public RatingNotFoundException() {
		super(ErrorCode.RATING_NOT_FOUND);
	}
}
