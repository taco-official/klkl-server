package taco.klkl.domain.user.domain;

import org.springframework.beans.factory.annotation.Value;

import taco.klkl.domain.image.domain.ImageType;

public class DefaultProfileImageUrlGenerator {

	@Value("${cloud.aws.cloudfront.domain}")
	private static String cloudFrontDomain;

	private static final String DEFAULT_PROFILE_IMAGE = "default.webp";

	public static String generate() {
		return "https://" + cloudFrontDomain + ImageType.USER_IMAGE.getValue() + "/" + DEFAULT_PROFILE_IMAGE;
	}
}
