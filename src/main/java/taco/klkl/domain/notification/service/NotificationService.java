package taco.klkl.domain.notification.service;

import java.util.List;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dto.response.NotificationDeleteResponse;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.dto.response.NotificationUpdateResponse;

public interface NotificationService {

	List<NotificationResponse> findAllNotifications();

	NotificationUpdateResponse readAllNotifications();

	NotificationUpdateResponse readNotificationById(final Long id);

	NotificationDeleteResponse deleteAllNotifications();

	void createNotificationByComment(final Comment comment);
}
