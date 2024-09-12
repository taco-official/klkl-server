package taco.klkl.domain.notification.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.domain.QMember;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.domain.QNotification;
import taco.klkl.domain.notification.dto.response.NotificationDeleteResponse;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.dto.response.NotificationUpdateResponse;
import taco.klkl.domain.notification.exception.NotificationNotFoundException;
import taco.klkl.global.util.MemberUtil;

/**
 * TODO: 알림은 30일 이내 데이터만 보여준다.
 * TODO: 30일이 지난 데이터는 삭제한다. (스프링 배치 스케줄링)
 */
@Slf4j
@Service
@Primary
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final JPAQueryFactory queryFactory;
	private final MemberUtil memberUtil;

	@Override
	public List<NotificationResponse> findAllNotifications() {
		final QNotification notification = QNotification.notification;
		final QMember member = QMember.member;
		final Member receiver = findReceiver();

		final List<Notification> notifications = queryFactory
			.selectFrom(notification)
			.join(notification.comment.product.member, member)
			.where(member.id.eq(receiver.getId()))
			.orderBy(notification.createdAt.desc(),
				notification.id.desc())
			.fetch();

		return notifications.stream()
			.map(NotificationResponse::from)
			.toList();
	}

	@Override
	@Transactional
	public NotificationUpdateResponse readAllNotifications() {
		final Member receiver = findReceiver();
		final List<Notification> notifications = notificationRepository.findByComment_Product_Member(receiver);
		notifications.forEach(Notification::read);
		final Long updatedCount = (long)notifications.size();
		return NotificationUpdateResponse.of(updatedCount);
	}

	@Override
	@Transactional
	public NotificationUpdateResponse readNotificationById(final Long id) {
		final Notification notification = notificationRepository.findById(id)
			.orElseThrow(NotificationNotFoundException::new);
		validateMyNotification(notification);
		notification.read();
		return NotificationUpdateResponse.of(1L);
	}

	@Override
	@Transactional
	public NotificationDeleteResponse deleteAllNotifications() {
		final Member receiver = findReceiver();
		final List<Notification> notifications = notificationRepository.findByComment_Product_Member(receiver);
		notificationRepository.deleteAll(notifications);
		final Long deletedCount = (long)notifications.size();
		return NotificationDeleteResponse.of(deletedCount);
	}

	@Override
	@Transactional
	public void createNotificationByComment(final Comment comment) {
		final Notification notification = Notification.of(comment);
		notificationRepository.save(notification);
	}

	// TODO: 토큰으로 유저 가져오는 방식으로 수정하기
	private Member findReceiver() {
		return memberUtil.getCurrentMember();
	}

	private void validateMyNotification(final Notification notification) {
		final Member receiver = findReceiver();
		if (!notification.getComment().getProduct().getMember().equals(receiver)) {
			throw new NotificationNotFoundException();
		}
	}
}
