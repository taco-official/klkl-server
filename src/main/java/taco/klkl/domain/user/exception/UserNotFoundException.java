package taco.klkl.domain.user.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class UserNotFoundException extends CustomException {
	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}
