package taco.klkl.domain.like.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class LikeCountMinimumException extends CustomException {
	public LikeCountMinimumException() {
		super(ErrorCode.LIKE_COUNT_MINIMUM);
	}
}
