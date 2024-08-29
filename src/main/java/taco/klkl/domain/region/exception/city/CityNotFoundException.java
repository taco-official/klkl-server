package taco.klkl.domain.region.exception.city;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CityNotFoundException extends CustomException {
	public CityNotFoundException() {
		super(ErrorCode.CITY_NOT_FOUND);
	}
}
