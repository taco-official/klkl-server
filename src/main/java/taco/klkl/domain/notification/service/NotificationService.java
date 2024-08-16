package taco.klkl.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dto.response.NotificationResponse;

@Service
public interface NotificationService {
	List<NotificationResponse> findAllNotifications();

	List<NotificationResponse> readAllNotifications();

	NotificationResponse readNotificationById(final Long id);

	void createNotificationByComment(final Comment comment);
}
