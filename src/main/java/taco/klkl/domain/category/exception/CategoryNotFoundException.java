package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CategoryNotFoundException extends CustomException {
	public CategoryNotFoundException() {
		super(ErrorCode.CATEGORY_ID_NOT_FOUND);
	}
}
