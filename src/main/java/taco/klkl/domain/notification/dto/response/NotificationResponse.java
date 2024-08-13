package taco.klkl.domain.notification.dto.response;

import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;

public record NotificationResponse(
	NotificationInfo notification,
	ProductSimpleResponseDto product,
	CommentResponseDto comment
) {
	public static NotificationResponse from(
		Notification notification
	) {
		return new NotificationResponse(
			NotificationInfo.from(notification),
			ProductSimpleResponseDto.from(notification.getComment().getProduct()),
			CommentResponseDto.from(notification.getComment())
		);
	}
}
