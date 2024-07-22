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
		User user = userService.me();

		// then
		mockMvc.perform(get("/v1/users/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(user.getId().intValue())))
			.andExpect(jsonPath("$.data.name", is(user.getName())))
			.andExpect(jsonPath("$.data.gender", is(user.getGender().toString())))
			.andExpect(jsonPath("$.data.age", is(user.getAge())))
			.andExpect(jsonPath("$.data.description", is(user.getDescription())))
			.andExpect(jsonPath("$.data.profile", is("image/test.jpg")))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
