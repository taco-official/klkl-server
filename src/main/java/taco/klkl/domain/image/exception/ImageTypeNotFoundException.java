package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ImageTypeNotFoundException extends CustomException {
	public ImageTypeNotFoundException() {
		super(ErrorCode.IMAGE_TYPE_NOT_FOUND);
	}
}
