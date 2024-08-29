package taco.klkl.global.common.constants;

import org.springframework.beans.factory.annotation.Value;

import taco.klkl.domain.image.domain.ImageType;
import taco.klkl.domain.user.domain.Gender;
import taco.klkl.domain.user.domain.User;

public final class UserConstants {

	@Value("${cloud.aws.cloudfront.domain}")
	private static String cloudFrontDomain;

	private static final String DEFAULT_PROFILE_IMAGE = "default.webp";

	public static final String TEST_USER_NAME = "testUser";
	public static final User TEST_USER = User.of(
		TEST_USER_NAME,
		Gender.MALE,
		20,
		"테스트입니다."
	);

	public static final String DEFAULT_PROFILE_IMAGE_URL
		= "https://" + cloudFrontDomain + ImageType.USER_IMAGE.getValue() + "/" + DEFAULT_PROFILE_IMAGE;
	public static final int DEFAULT_TOTAL_LIKE_COUNT = 0;
	public static final int USERNAME_SUFFIX_MOD = 9973;

	private UserConstants() {
	}
}
