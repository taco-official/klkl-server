package taco.klkl.domain.oauth2.dto.request;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record KakaoUserInfoRequest(Long id, String nickname, String profileImage) {

	public static KakaoUserInfoRequest of(final Long oauth2MemberId, final String nickname, final String profileImage) {
		return new KakaoUserInfoRequest(oauth2MemberId, nickname, profileImage);
	}
}
