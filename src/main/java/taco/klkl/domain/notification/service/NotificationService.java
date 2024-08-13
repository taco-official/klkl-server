package taco.klkl.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.global.common.response.PagedResponseDto;

@Service
public interface NotificationService {
	PagedResponseDto<NotificationResponse> getNotifications(Pageable pageable);

	List<NotificationResponse> readAllNotifications();

	NotificationResponse readNotificationById(long id);

	void createNotification(Comment comment);
}
