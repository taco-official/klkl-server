package taco.klkl.domain.notification.dto.response;

import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;

public record NotificationResponse(
	NotificationInfo notification,
	ProductSimpleResponse product,
	CommentResponse comment
) {
	public static NotificationResponse from(final Notification notification) {
		return new NotificationResponse(
			NotificationInfo.from(notification),
			ProductSimpleResponse.from(notification.getComment().getProduct()),
			CommentResponse.from(notification.getComment())
		);
	}
}
