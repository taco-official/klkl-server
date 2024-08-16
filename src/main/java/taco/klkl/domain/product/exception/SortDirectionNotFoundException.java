package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class SortDirectionNotFoundException extends CustomException {
	public SortDirectionNotFoundException() {
		super(ErrorCode.SORT_DIRECTION_NOT_FOUND);
	}
}
