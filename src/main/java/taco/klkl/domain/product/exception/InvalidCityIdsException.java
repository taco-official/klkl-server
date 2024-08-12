package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class InvalidCityIdsException extends CustomException {
	public InvalidCityIdsException() {
		super(ErrorCode.INVALID_CITY_IDS);
	}
}
