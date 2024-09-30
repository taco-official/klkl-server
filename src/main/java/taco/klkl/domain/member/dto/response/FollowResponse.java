package taco.klkl.domain.member.dto.response;

import taco.klkl.domain.member.domain.Member;

public record FollowResponse(
	boolean isFollowing,
	Long followerId,
	Long followingId
) {
	public static FollowResponse of(final boolean isFollowing, final Member follower, final Member following) {
		return new FollowResponse(
			isFollowing,
			follower.getId(),
			following.getId()
		);
	}
}
