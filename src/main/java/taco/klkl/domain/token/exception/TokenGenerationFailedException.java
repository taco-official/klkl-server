package taco.klkl.domain.token.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class TokenGenerationFailedException extends CustomException {
	public TokenGenerationFailedException() {
		super(ErrorCode.TOKEN_GENERATION_FAILED);
	}
}
