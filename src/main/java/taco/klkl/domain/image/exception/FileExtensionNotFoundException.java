package taco.klkl.domain.image.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class FileExtensionNotFoundException extends CustomException {
	public FileExtensionNotFoundException() {
		super(ErrorCode.FILE_EXTENSION_NOT_FOUND);
	}
}
