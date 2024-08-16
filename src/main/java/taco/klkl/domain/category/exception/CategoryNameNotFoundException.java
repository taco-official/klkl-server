package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CategoryNameNotFoundException extends CustomException {
	public CategoryNameNotFoundException() {
		super(ErrorCode.CATEGORY_NAME_NOT_FOUND);
	}
}
