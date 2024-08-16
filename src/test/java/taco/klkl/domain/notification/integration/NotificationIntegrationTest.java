package taco.klkl.domain.notification.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.global.error.exception.ErrorCode;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NotificationIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private NotificationService notificationService;

	@Test
	@DisplayName("모든 알림 조회 테스트")
	public void testGetAllNotifications() throws Exception {
		//given
		List<NotificationResponse> responses = notificationService.findAllNotifications();

		//when & then
		mockMvc.perform(get("/v1/notifications")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(responses.size())));
	}

	@Test
	@DisplayName("모든 알림 읽음 테스트")
	public void testReadAllNotification() throws Exception {
		//given
		//when & then
		mockMvc.perform(put("/v1/notifications/all")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data[*].notification.isRead", everyItem(is(true))));
	}

	@Test
	@DisplayName("단일 알림 읽음 테스트")
	public void testReadOneNotification() throws Exception {
		//given
		Long notificationId = 700L;

		//when & then
		mockMvc.perform(put("/v1/notifications/{notificationId}", notificationId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.notification.isRead", is(true)));
	}

	@Test
	@DisplayName("단일 알림 읽음 테스트")
	public void testReadOneNotificationWithNotFound() throws Exception {
		//given
		Long wrongNotificationId = 1L;

		//when & then
		mockMvc.perform(put("/v1/notifications/{wrongNotificationId}", wrongNotificationId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.NOTIFICATION_NOT_FOUND.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.NOTIFICATION_NOT_FOUND.getMessage())));
	}
}
