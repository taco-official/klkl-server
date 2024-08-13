package taco.klkl.domain.notification.service;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.domain.QNotification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.exception.NotificationNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.response.PagedResponseDto;
import taco.klkl.global.util.UserUtil;

@Service
@Primary
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
	private final NotificationRepository notificationRepository;
	private final JPAQueryFactory queryFactory;
	private final UserUtil userUtil;

	@Override
	public PagedResponseDto<NotificationResponse> getNotifications(Pageable pageable) {

		User receiver = getReceiver();

		QNotification notification = QNotification.notification;
		BooleanBuilder builder = buildUserOptions(receiver, notification);

		List<Notification> notifications = queryFactory.selectFrom(notification)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.select(notification.count())
			.where(builder)
			.from(notification)
			.fetchOne();

		List<NotificationResponse> notificationResponses = notifications.stream()
			.map(NotificationResponse::from)
			.toList();

		Page<NotificationResponse> notificationPage = new PageImpl<>(notificationResponses, pageable, total);

		return PagedResponseDto.of(notificationPage, Function.identity());
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

	private BooleanBuilder buildUserOptions(User user, QNotification notification) {
		BooleanBuilder builder = new BooleanBuilder();

		if (user != null) {
			builder.and(notification.comment.product.user.id.eq(user.getId()));
		}
		return builder;
	}
}
