package taco.klkl.domain.notification.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class NotificationException extends CustomException {
	public NotificationException() {
		super(ErrorCode.NOTIFICATION_NOT_FOUND);
	}
}
