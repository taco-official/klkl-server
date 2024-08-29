package taco.klkl.domain.category.exception.tag;

import taco.klkl.global.error.exception.CustomException;
import taco.klkl.global.error.exception.ErrorCode;

public class TagTypeNotFoundException extends CustomException {
	public TagTypeNotFoundException() {
		super(ErrorCode.TAG_TYPE_NOT_FOUND);
	}
}
