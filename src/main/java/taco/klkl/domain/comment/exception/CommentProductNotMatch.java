package taco.klkl.domain.comment.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class CommentProductNotMatch extends CustomException {
	public CommentProductNotMatch() {
		super(ErrorCode.COMMENT_PRODUCT_NOT_MATCH);
	}
}
