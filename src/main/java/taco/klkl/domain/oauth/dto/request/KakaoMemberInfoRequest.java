package taco.klkl.domain.oauth.dto.request;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record KakaoMemberInfoRequest(Long oauthMemberId, String nickname, String profileImage) {

	public static KakaoMemberInfoRequest of(
		final Long oauthMemberId,
		final String nickname,
		final String profileImage
	) {
		return new KakaoMemberInfoRequest(oauthMemberId, nickname, profileImage);
	}
}
