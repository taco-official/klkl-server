package taco.klkl.domain.product.exception;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class ProductNotFoundException extends CustomException {
	public ProductNotFoundException() {
		super(ErrorCode.PRODUCT_NOT_FOUND);
	}
}
