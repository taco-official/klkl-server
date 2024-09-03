package taco.klkl.domain.category.exception.category;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CategoryTypeNotFoundException extends CustomException {
	public CategoryTypeNotFoundException() {
		super(ErrorCode.TAG_TYPE_NOT_FOUND);
	}
}
