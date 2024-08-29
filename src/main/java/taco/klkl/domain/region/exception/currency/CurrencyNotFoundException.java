package taco.klkl.domain.region.exception.currency;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CurrencyNotFoundException extends CustomException {
	public CurrencyNotFoundException() {
		super(ErrorCode.CURRENCY_NOT_FOUND);
	}
}
