package taco.klkl.domain.member.dto.response;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.domain.Member;

public record MemberSimpleResponse(
	Long id,
	ImageResponse image,
	String name
) {
	public static MemberSimpleResponse from(final Member member) {
		return new MemberSimpleResponse(
			member.getId(),
			ImageResponse.from(member.getProfileImage()),
			member.getDisplayName()
		);
	}
}
