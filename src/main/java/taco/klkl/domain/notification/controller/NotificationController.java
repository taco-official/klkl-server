package taco.klkl.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.service.NotificationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notifications")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public List<NotificationResponse> getAllNotifications() {
		return notificationService.getNotifications();
	}

	@PutMapping("/all")
	public List<NotificationResponse> readAllNotifications() {
		return notificationService.readAllNotifications();
	}

	@PutMapping("/{notificationId}")
	public NotificationResponse readNotification(@PathVariable Long notificationId) {
		return notificationService.readNotificationById(notificationId);
	}
}
