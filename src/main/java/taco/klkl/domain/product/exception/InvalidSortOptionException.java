package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class InvalidSortOptionException extends CustomException {
	public InvalidSortOptionException() {
		super(ErrorCode.INVALID_SORT_OPTION);
	}
}
