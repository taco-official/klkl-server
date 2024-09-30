package taco.klkl.domain.auth.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class PermissionDeniedException extends CustomException {
	public PermissionDeniedException() {
		super(ErrorCode.PERMISSION_DENIED);
	}
}
