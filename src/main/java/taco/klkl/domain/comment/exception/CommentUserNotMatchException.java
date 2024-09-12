package taco.klkl.domain.comment.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CommentUserNotMatchException extends CustomException {
	public CommentUserNotMatchException() {
		super(ErrorCode.COMMENT_USER_NOT_MATCH);
	}
}
