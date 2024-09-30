package taco.klkl.domain.auth.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class UnauthorizedException extends CustomException {
	public UnauthorizedException() {
		super(ErrorCode.UNAUTHORIZED);
	}
}
