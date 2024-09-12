package taco.klkl.domain.member.dto.response;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.domain.Member;

public record MemberDetailResponse(
	Long id,
	ImageResponse image,
	String name,
	String description
) {
	public static MemberDetailResponse from(final Member member) {
		return new MemberDetailResponse(
			member.getId(),
			ImageResponse.from(member.getImage()),
			member.getName(),
			member.getDescription()
		);
	}
}
