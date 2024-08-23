package taco.klkl.domain.image.domain;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.exception.ImageTypeNotFoundException;

@Getter
@RequiredArgsConstructor
public enum ImageType {
	USER_PROFILE("user_profile"),
	PRODUCT_IMAGE("product_image"),
	;

	private final String value;

	public static ImageType from(final String value) {
		return Arrays.stream(ImageType.values())
			.filter(type -> type.getValue().equals(value))
			.findFirst()
			.orElseThrow(ImageTypeNotFoundException::new);
	}
}
