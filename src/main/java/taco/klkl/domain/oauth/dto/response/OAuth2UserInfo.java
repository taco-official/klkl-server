package taco.klkl.domain.oauth.dto.response;

import java.util.Map;

import taco.klkl.domain.member.domain.Member;

public record OAuth2UserInfo(
	String name,
	String profile
) {

	public static OAuth2UserInfo of(
		final String registrationId,
		Map<String, Object> attributes
	) {
		return switch (registrationId) {
			case "kakao" -> ofKakao(attributes);
			default -> throw new IllegalArgumentException();
		};
	}

	public static OAuth2UserInfo ofKakao(
		final Map<String, Object> attributes
	) {
		final Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		final Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

		return new OAuth2UserInfo(
			(String)kakaoProfile.get("nickname"),
			(String)kakaoProfile.get("profile_image_url")
		);
	}

	public Member toEntity() {
		return Member.of(name, "");
	}
}
