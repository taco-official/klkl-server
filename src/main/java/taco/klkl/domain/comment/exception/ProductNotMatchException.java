package taco.klkl.domain.comment.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ProductNotMatchException extends CustomException {
	public ProductNotMatchException() {
		super(ErrorCode.PRODUCT_NOT_MATCH);
	}
}
