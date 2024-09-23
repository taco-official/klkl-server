package taco.klkl.domain.token.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class TokenExpiredException extends CustomException {
	public TokenExpiredException() {
		super(ErrorCode.TOKEN_EXPIRED);
	}
}
