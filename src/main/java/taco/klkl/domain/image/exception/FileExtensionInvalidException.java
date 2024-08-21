package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class FileExtensionInvalidException extends CustomException {
	public FileExtensionInvalidException() {
		super(ErrorCode.FILE_EXTENSION_INVALID);
	}
}
