package taco.klkl.domain.comment.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CommentProductNotMatchException extends CustomException {
	public CommentProductNotMatchException() {
		super(ErrorCode.COMMENT_PRODUCT_NOT_MATCH);
	}
}
