package taco.klkl.domain.notification.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static taco.klkl.global.common.constants.TestConstants.TEST_UUID;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.error.exception.ErrorCode;
import taco.klkl.global.util.ResponseUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig.class)
@WithMockUser(username = TEST_UUID, roles = "USER")
public class NotificationIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

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
			.andExpect(jsonPath("$.data", hasSize(responses.size())));
	}

	@Test
	@DisplayName("모든 알림 읽음 테스트")
	public void testReadAllNotifications() throws Exception {
		//given
		//when & then
		mockMvc.perform(put("/v1/notifications/read")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.updatedCount", not(0)));
	}

	@Test
	@DisplayName("단일 알림 읽음 테스트 - 성공")
	public void testReadOneNotification() throws Exception {
		//given
		Long notificationId = 701L;

		//when & then
		mockMvc.perform(put("/v1/notifications/{notificationId}/read", notificationId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.updatedCount", is(1)));
	}

	@Test
	@DisplayName("단일 알림 읽음 테스트 - 실패")
	public void testReadOneNotificationWithNotFound() throws Exception {
		//given
		Long wrongNotificationId = 1L;

		//when & then
		mockMvc.perform(put("/v1/notifications/{wrongNotificationId}/read", wrongNotificationId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.status", is(ErrorCode.NOTIFICATION_NOT_FOUND.getHttpStatus().value())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.NOTIFICATION_NOT_FOUND.getMessage())));
	}
}
