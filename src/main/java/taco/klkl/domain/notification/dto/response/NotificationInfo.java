package taco.klkl.domain.notification.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.notification.domain.Notification;

public record NotificationInfo(
	long id,
	boolean isRead,
	LocalDateTime createdAt
) {
	public static NotificationInfo from(final Notification notification) {
		return new NotificationInfo(
			notification.getId(),
			notification.getIsRead(),
			notification.getCreatedAt()
		);
	}
}
