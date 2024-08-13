package taco.klkl.domain.like.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class LikeCountBelowMinimumException extends CustomException {
	public LikeCountBelowMinimumException() {
		super(ErrorCode.LIKE_COUNT_BELOW_MINIMUM);
	}
}
