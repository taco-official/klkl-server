package taco.klkl.domain.user.dto.response;

import taco.klkl.domain.user.domain.User;

public record UserFollowResponse(
	boolean isFollowing,
	Long followerId,
	Long followingId
) {
	public static UserFollowResponse of(final boolean isFollowing, final User follower, final User following) {
		return new UserFollowResponse(
			isFollowing,
			follower.getId(),
			following.getId()
		);
	}
}
