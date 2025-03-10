package taco.klkl.domain.notification.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.notification.dto.response.NotificationDeleteResponse;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.dto.response.NotificationUpdateResponse;
import taco.klkl.domain.notification.service.NotificationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notifications")
@Tag(name = "7. 알림", description = "알림관련 API")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	@Operation(summary = "알림 목록 조회", description = "알림 전체 목록을 조회합니다.")
	public List<NotificationResponse> findAllNotifications() {
		return notificationService.findAllNotifications();
	}

	@PutMapping("/read")
	@Operation(summary = "모든 알림 읽음", description = "모든 알림을 읽음으로 처리합니다.")
	public NotificationUpdateResponse readAllNotifications() {
		return notificationService.readAllNotifications();
	}

	@PutMapping("/{notificationId}/read")
	@Operation(summary = "단일 알림 읽음", description = "단일 알림을 읽음으로 처리합니다.")
	public NotificationUpdateResponse readOneNotification(@PathVariable final Long notificationId) {
		return notificationService.readNotificationById(notificationId);
	}

	@DeleteMapping
	@Operation(summary = "모든 알림 삭제", description = "모든 알림을 삭제합니다.")
	public NotificationDeleteResponse deleteAllNotifications() {
		return notificationService.deleteAllNotifications();
	}
}
