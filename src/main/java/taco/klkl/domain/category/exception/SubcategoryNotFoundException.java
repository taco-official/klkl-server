package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class SubcategoryNotFoundException extends CustomException {
	public SubcategoryNotFoundException() {
		super(ErrorCode.SUBCATEGORY_ID_NOT_FOUND);
	}
}
