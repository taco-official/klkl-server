package taco.klkl.domain.user.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class GenderNotFoundException extends CustomException {
	public GenderNotFoundException() {
		super(ErrorCode.GENDER_NOT_FOUND);
	}
}
