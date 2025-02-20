package taco.klkl.domain.image.service;

import java.util.UUID;

public final class ImageKeyGenerator {

	public static String generate() {
		return UUID.randomUUID().toString();
	}
}
