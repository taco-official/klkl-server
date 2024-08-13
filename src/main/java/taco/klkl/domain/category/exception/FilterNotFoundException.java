package taco.klkl.domain.category.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class FilterNotFoundException extends CustomException {
	public FilterNotFoundException() {
		super(ErrorCode.FILTER_NOT_FOUND);
	}
}
