package taco.klkl.domain.member.domain;

import java.util.UUID;

public final class HandleGenerator {

	private static final String DEFAULT_PREFIX = "user_";

	public static String generate() {
		final String randomString = UUID.randomUUID().toString().substring(0, 8);
		return DEFAULT_PREFIX + randomString;
	}

	private HandleGenerator() {
	}
}
