package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class TagNotFoundException extends CustomException {
	public TagNotFoundException() {
		super(ErrorCode.TAG_NOT_FOUND);
	}
}
