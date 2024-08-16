package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class SubcategoryNameNotFoundException extends CustomException {
	public SubcategoryNameNotFoundException() {
		super(ErrorCode.SUBCATEGORY_NAME_NOT_FOUND);
	}
}
