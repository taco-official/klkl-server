package taco.klkl.domain.category.exception.subcategory;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class SubcategoryTypeNotFoundException extends CustomException {
	public SubcategoryTypeNotFoundException() {
		super(ErrorCode.SUBCATEGORY_TYPE_NOT_FOUND);
	}
}
