package taco.klkl.domain.region.exception.region;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class RegionNotFoundException extends CustomException {
	public RegionNotFoundException() {
		super(ErrorCode.REGION_NOT_FOUND);
	}
}
