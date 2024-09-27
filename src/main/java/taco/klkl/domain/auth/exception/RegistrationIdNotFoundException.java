package taco.klkl.domain.auth.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class RegistrationIdNotFoundException extends CustomException {
	public RegistrationIdNotFoundException() {
		super(ErrorCode.REGISTRATION_ID_NOT_FOUND);
	}
}
