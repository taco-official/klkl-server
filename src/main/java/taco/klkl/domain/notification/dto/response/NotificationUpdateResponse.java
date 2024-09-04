package taco.klkl.domain.notification.dto.response;

public record NotificationUpdateResponse(
	Long updatedCount
) {
	public static NotificationUpdateResponse of(final Long updatedCount) {
		return new NotificationUpdateResponse(updatedCount);
	}
}
