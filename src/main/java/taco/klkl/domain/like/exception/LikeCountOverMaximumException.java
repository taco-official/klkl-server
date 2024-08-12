package taco.klkl.domain.like.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class LikeCountOverMaximumException extends CustomException {
	public LikeCountOverMaximumException() {
		super(ErrorCode.LIKES_OVER_MAXIMUM);
	}
}
