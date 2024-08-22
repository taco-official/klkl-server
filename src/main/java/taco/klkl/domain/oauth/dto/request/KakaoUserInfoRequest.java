package taco.klkl.domain.oauth.dto.request;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record KakaoUserInfoRequest(Long id, String nickname, String profileImage) {

	public static KakaoUserInfoRequest of(final Long oauthMemberId, final String nickname, final String profileImage) {
		return new KakaoUserInfoRequest(oauthMemberId, nickname, profileImage);
	}
}
