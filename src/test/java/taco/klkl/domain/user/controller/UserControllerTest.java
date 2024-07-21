package taco.klkl.domain.user.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.service.UserService;
import taco.klkl.global.common.enums.Gender;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;

	private User user;

	@BeforeEach
	public void setUp() {
		user = new User(null, "testUser", Gender.MALE, 20, "테스트 유저입니다.");
	}

	@Test
	public void getMe() throws Exception {
		// given
		Mockito.when(userService.me()).thenReturn(user);

		// when & then
		mockMvc.perform(get("/api/v1/users/me")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(nullValue())))
			.andExpect(jsonPath("$.data.name", is(user.getName())))
			.andExpect(jsonPath("$.data.gender", is(user.getGender().toString())))
			.andExpect(jsonPath("$.data.age", is(user.getAge())))
			.andExpect(jsonPath("$.data.description", is(user.getDescription())))
			.andExpect(jsonPath("$.data.profile", is(nullValue())))
			.andExpect(jsonPath("$.data.created_at", is(nullValue())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
