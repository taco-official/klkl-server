package taco.klkl.domain.member.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class SelfFollowNotAllowedException extends CustomException {
	public SelfFollowNotAllowedException() {
		super(ErrorCode.SELF_FOLLOW_NOT_ALLOWED);
	}
}
