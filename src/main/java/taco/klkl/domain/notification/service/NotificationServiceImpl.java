package taco.klkl.domain.notification.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.exception.NotificationNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

/**
 * TODO: 알림은 30일 이내 데이터만 보여준다.
 * TODO: 페이징 하지않고 모든 데이터를 준다.
 * TODO: 30일이 지난 데이터는 삭제한다. (스프링 배치 스케줄링)
 */
@Slf4j
@Service
@Primary
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserUtil userUtil;

	@Override
	public List<NotificationResponse> getNotifications() {

		User receiver = getReceiver();
		List<Notification> notifications = notificationRepository.findAllByComment_Product_User(receiver);

		return notifications.stream()
			.map(NotificationResponse::from)
			.toList();
	}

	@Override
	@Transactional
	public List<NotificationResponse> readAllNotifications() {

		User receiver = getReceiver();
		List<Notification> notifications = notificationRepository.findAllByComment_Product_User(receiver);

		return notifications.stream()
			.map(n -> {
				n.read();
				return NotificationResponse.from(n);
			}).toList();
	}

	@Override
	@Transactional
	public NotificationResponse readNotificationById(long id) {

		Notification notification = notificationRepository.findById(id)
			.orElseThrow(NotificationNotFoundException::new);
		notification.read();
		return NotificationResponse.from(notification);
	}

	@Override
	@Transactional
	public void createNotification(Comment comment) {

		Notification notification = Notification.of(comment);
		notificationRepository.save(notification);
	}

	// TODO: 토큰으로 유저 가져오는 방식으로 수정하기
	private User getReceiver() {
		return userUtil.findTestUser();
	}
}
