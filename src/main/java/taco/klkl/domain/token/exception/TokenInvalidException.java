package taco.klkl.domain.token.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class TokenInvalidException extends CustomException {
	public TokenInvalidException() {
		super(ErrorCode.TOKEN_INVALID);
	}
}
