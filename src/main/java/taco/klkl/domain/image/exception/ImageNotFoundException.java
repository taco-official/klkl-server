package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ImageNotFoundException extends CustomException {
	public ImageNotFoundException() {
		super(ErrorCode.IMAGE_NOT_FOUND);
	}
}
