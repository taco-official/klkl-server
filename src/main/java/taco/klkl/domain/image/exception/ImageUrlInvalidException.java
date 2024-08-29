package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ImageUrlInvalidException extends CustomException {
	public ImageUrlInvalidException() {
		super(ErrorCode.IMAGE_URL_INVALID);
	}
}
