package taco.klkl.domain.region.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CountryNotFoundException extends CustomException {
	public CountryNotFoundException() {
		super(ErrorCode.COUNTRY_NOT_FOUND);
	}
}
