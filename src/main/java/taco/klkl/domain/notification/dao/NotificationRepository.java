package taco.klkl.domain.notification.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByComment_Product_Member(final Member member);
}
