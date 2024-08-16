package taco.klkl.domain.region.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CountryTypeNotFoundException extends CustomException {
	public CountryTypeNotFoundException() {
		super(ErrorCode.COUNTRY_TYPE_NOT_FOUND);
	}
}
