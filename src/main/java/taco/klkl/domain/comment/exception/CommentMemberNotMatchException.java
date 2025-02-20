package taco.klkl.domain.comment.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CommentMemberNotMatchException extends CustomException {
	public CommentMemberNotMatchException() {
		super(ErrorCode.COMMENT_MEMBER_NOT_MATCH);
	}
}
