package taco.klkl.domain.user.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.service.UserService;
import taco.klkl.global.common.constants.UserConstants;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	private UserDetailResponse responseDto;

	@BeforeEach
	public void setUp() {
		final User user = UserConstants.TEST_USER;
		System.out.println("Test User created - profileImageUrl: " + user.getProfileImageUrl());
		responseDto = UserDetailResponse.from(user);
		System.out.println("Test Response DTO: " + responseDto);
	}

	@Test
	@DisplayName("내 정보 조회 API 테스트")
	public void testGetMe() throws Exception {
		// given
		Mockito.when(userService.getCurrentUser()).thenReturn(responseDto);
		UserDetailResponse mockedResponse = userService.getCurrentUser();
		System.out.println("Mocked Response: " + mockedResponse);

		// when & then
		mockMvc.perform(get("/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(nullValue())))
			.andExpect(jsonPath("$.data.profileImageUrl", is(notNullValue())))
			.andExpect(jsonPath("$.data.name", is(responseDto.name())))
			.andExpect(jsonPath("$.data.description", is(responseDto.description())))
			.andExpect(jsonPath("$.data.totalLikeCount", is(UserConstants.DEFAULT_TOTAL_LIKE_COUNT)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
