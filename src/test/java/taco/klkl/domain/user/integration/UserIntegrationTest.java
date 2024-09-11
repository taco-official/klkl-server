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
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.service.UserService;
import taco.klkl.global.util.UserUtil;

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

	@Autowired
	private UserUtil userUtil;

	@Test
	public void testUserMe() throws Exception {
		// given, when
		User me = userUtil.getCurrentUser();
		UserDetailResponse currentUser = userService.getUserById(me.getId());

		// then
		mockMvc.perform(get("/v1/users/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(currentUser.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(currentUser.name())))
			.andExpect(jsonPath("$.data.description", is(currentUser.description())))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
		;
	}
}
