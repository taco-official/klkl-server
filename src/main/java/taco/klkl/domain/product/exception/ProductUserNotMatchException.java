package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ProductUserNotMatchException extends CustomException {
	public ProductUserNotMatchException() {
		super(ErrorCode.PRODUCT_USER_NOT_MATCH);
	}
}
