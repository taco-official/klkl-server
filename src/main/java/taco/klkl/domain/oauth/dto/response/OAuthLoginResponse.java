package taco.klkl.domain.oauth.dto.response;

import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;

public record OAuthLoginResponse(
	String accessToken,
	String refreshToken,
	MemberDetailResponse member
) {
	public static OAuthLoginResponse of(
		final String accessToken,
		final String refreshToken,
		final Member member
	) {
		return new OAuthLoginResponse(
			accessToken,
			refreshToken,
			MemberDetailResponse.from(member)
		);
	}
}
