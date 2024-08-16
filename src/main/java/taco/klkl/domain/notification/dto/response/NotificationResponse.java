package taco.klkl.domain.notification.dto.response;

import taco.klkl.domain.comment.dto.response.CommentResponse;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;

public record NotificationResponse(
	NotificationInfo notification,
	ProductSimpleResponseDto product,
	CommentResponse comment
) {
	public static NotificationResponse from(
		Notification notification
	) {
		return new NotificationResponse(
			NotificationInfo.from(notification),
			ProductSimpleResponseDto.from(notification.getComment().getProduct()),
			CommentResponse.from(notification.getComment())
		);
	}
}
