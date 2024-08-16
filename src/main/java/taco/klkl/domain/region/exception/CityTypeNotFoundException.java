package taco.klkl.domain.region.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CityTypeNotFoundException extends CustomException {
	public CityTypeNotFoundException() {
		super(ErrorCode.CITY_TYPE_NOT_FOUND);
	}
}
