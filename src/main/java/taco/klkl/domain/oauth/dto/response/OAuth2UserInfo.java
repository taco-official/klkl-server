package taco.klkl.domain.oauth.dto.response;

import java.util.Map;

import taco.klkl.domain.oauth.exception.RegistrationIdNotFoundException;

public record OAuth2UserInfo(
	String name,
	String imageUrl,
	String provider,
	String providerId
) {

	public static OAuth2UserInfo of(
		final String registrationId,
		Map<String, Object> attributes
	) {
		return switch (registrationId) {
			case "kakao" -> ofKakao(attributes);
			default -> throw new RegistrationIdNotFoundException();
		};
	}

	public static OAuth2UserInfo ofKakao(
		final Map<String, Object> attributes
	) {
		final Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		final Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

		return new OAuth2UserInfo(
			(String)kakaoProfile.get("nickname"),
			(String)kakaoProfile.get("profile_image_url"),
			"kakao",
			String.valueOf(attributes.get("id"))
		);
	}
}
