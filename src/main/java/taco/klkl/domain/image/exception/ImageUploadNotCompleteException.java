package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ImageUploadNotCompleteException extends CustomException {
	public ImageUploadNotCompleteException() {
		super(ErrorCode.IMAGE_UPLOAD_NOT_COMPLETE);
	}
}
