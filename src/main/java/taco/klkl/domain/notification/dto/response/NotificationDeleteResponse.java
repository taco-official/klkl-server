package taco.klkl.domain.notification.dto.response;

public record NotificationDeleteResponse(
	Long deletedCount
) {
	public static NotificationDeleteResponse of(final Long deletedCount) {
		return new NotificationDeleteResponse(deletedCount);
	}
}
