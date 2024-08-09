package taco.klkl.domain.like.dto.response;

public record LikeResponseDto(
	boolean isLiked,
	int likeCount
) {
	public static LikeResponseDto of(boolean isLiked, int likeCount) {
		return new LikeResponseDto(isLiked, likeCount);
	}
}
