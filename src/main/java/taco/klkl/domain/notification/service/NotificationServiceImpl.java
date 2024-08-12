package taco.klkl.domain.notification.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.exception.NotificationException;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

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
			.map(n -> {
				Comment comment = n.getComment();
				Product product = comment.getProduct();
				return NotificationResponse.from(n, product, comment);
			}).toList();
	}

	@Override
	@Transactional
	public List<NotificationResponse> readAllNotifications() {

		User receiver = getReceiver();
		List<Notification> notifications = notificationRepository.findAllByComment_Product_User(receiver);

		return notifications.stream()
			.map(n -> {
				n.read();
				Comment comment = n.getComment();
				Product product = comment.getProduct();
				return NotificationResponse.from(n, product, comment);
			}).toList();
	}

	@Override
	@Transactional
	public NotificationResponse readNotificationById(long id) {

		Notification notification = notificationRepository.findById(id)
			.orElseThrow(NotificationException::new);
		notification.read();
		Comment comment = notification.getComment();
		Product product = comment.getProduct();
		return NotificationResponse.from(notification, product, comment);
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
