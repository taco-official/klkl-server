package taco.klkl.domain.user.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Test
	public void testUserMe() throws Exception {
		// given, when
		UserDetailResponse userDto = userService.getCurrentUser();

		// then
		mockMvc.perform(get("/v1/users/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(userDto.id().intValue())))
			.andExpect(jsonPath("$.data.profileImageUrl", is(userDto.profileImageUrl())))
			.andExpect(jsonPath("$.data.name", is(userDto.name())))
			.andExpect(jsonPath("$.data.description", is(userDto.description())))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
		;
	}
}
