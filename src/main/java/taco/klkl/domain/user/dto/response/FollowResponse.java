package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record FollowResponse(
	boolean isFollowing,
	Long followerId,
	Long followingId
) {
	public static FollowResponse of(final boolean isFollowing, final User follower, final User following) {
		return new FollowResponse(
			isFollowing,
			follower.getId(),
			following.getId()
		);
	}
}
