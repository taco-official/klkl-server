package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class UploadStateNotFoundException extends CustomException {
	public UploadStateNotFoundException() {
		super(ErrorCode.UPLOAD_STATE_NOT_FOUND);
	}
}
