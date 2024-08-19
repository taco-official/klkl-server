package taco.klkl.domain.like.dto.response;

public record LikeResponse(
	boolean isLiked,
	int likeCount
) {
	public static LikeResponse of(boolean isLiked, int likeCount) {
		return new LikeResponse(isLiked, likeCount);
	}
}
