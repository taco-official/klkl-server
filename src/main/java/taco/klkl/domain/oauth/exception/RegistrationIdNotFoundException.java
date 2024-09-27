package taco.klkl.domain.oauth.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class RegistrationIdNotFoundException extends CustomException {
	public RegistrationIdNotFoundException() {
		super(ErrorCode.REGISTRATION_ID_NOT_FOUND);
	}
}
