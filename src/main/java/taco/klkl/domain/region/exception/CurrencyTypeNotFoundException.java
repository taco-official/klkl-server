package taco.klkl.domain.region.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CurrencyTypeNotFoundException extends CustomException {
	public CurrencyTypeNotFoundException() {
		super(ErrorCode.CURRENCY_TYPE_NOT_FOUND);
	}
}
