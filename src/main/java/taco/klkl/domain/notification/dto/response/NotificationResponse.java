package taco.klkl.domain.notification.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.comment.dto.response.CommentNotificationResponse;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.product.dto.response.ProductNotificationResponse;

public record NotificationResponse(
	Long id,
	boolean isRead,
	ProductNotificationResponse product,
	CommentNotificationResponse comment,
	LocalDateTime createdAt
) {
	public static NotificationResponse from(final Notification notification) {
		return new NotificationResponse(
			notification.getId(),
			notification.getIsRead(),
			ProductNotificationResponse.from(notification.getComment().getProduct()),
			CommentNotificationResponse.from(notification.getComment()),
			notification.getCreatedAt()
		);
	}
}
