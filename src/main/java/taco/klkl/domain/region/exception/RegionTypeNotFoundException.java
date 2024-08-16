package taco.klkl.domain.region.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class RegionTypeNotFoundException extends CustomException {
	public RegionTypeNotFoundException() {
		super(ErrorCode.REGION_TYPE_NOT_FOUND);
	}
}
