package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class SortCriteriaNotFoundException extends CustomException {
	public SortCriteriaNotFoundException() {
		super(ErrorCode.SORT_CRITERIA_NOT_FOUND);
	}
}
