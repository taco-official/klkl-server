package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class TagNameNotFoundException extends CustomException {
	public TagNameNotFoundException() {
		super(ErrorCode.CATEGORY_NAME_NOT_FOUND);
	}
}
