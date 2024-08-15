package taco.klkl.domain.notification.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.user.domain.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findAllByComment_Product_User(User user);
}
