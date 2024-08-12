package taco.klkl.domain.notification.dto.response;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.dto.response.CommentResponseDto;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;

public record NotificationResponse(
	NotificationInfo notification,
	ProductSimpleResponseDto product,
	CommentResponseDto comment
) {
	public static NotificationResponse from(
		Notification notification,
		Product product,
		Comment comment
	) {
		return new NotificationResponse(
			NotificationInfo.from(notification),
			ProductSimpleResponseDto.from(product),
			CommentResponseDto.from(comment)
		);
	}
}
