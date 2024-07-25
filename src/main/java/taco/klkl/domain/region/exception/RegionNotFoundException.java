package taco.klkl.domain.region.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class RegionNotFoundException extends CustomException {
	public RegionNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
