package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ProductMemberNotMatchException extends CustomException {
	public ProductMemberNotMatchException() {
		super(ErrorCode.PRODUCT_MEMBER_NOT_MATCH);
	}
}
