package taco.klkl.domain.like.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class LikeCountMaximumException extends CustomException {
	public LikeCountMaximumException() {
		super(ErrorCode.LIKE_COUNT_MAXIMUM);
	}
}
