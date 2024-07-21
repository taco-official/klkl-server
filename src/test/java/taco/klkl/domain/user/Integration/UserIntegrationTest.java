package taco.klkl.domain.user.Integration;

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
import taco.klkl.global.common.enums.Gender;

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

	User user;

	@Test
	public void testUserMe() throws Exception {
		// given
		User user = new User(null, "testUser", Gender.MALE, 20, "테스트 유저입니다.");

		// when
		userRepository.save(user);
		user = userService.me();

		// then
		mockMvc.perform(get("/api/v1/users/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(user.getId().intValue())))
			.andExpect(jsonPath("$.data.name", is(user.getName())))
			.andExpect(jsonPath("$.data.gender", is(user.getGender().toString())))
			.andExpect(jsonPath("$.data.age", is(user.getAge())))
			.andExpect(jsonPath("$.data.description", is(user.getDescription())))
			.andExpect(jsonPath("$.data.profile", is("image/default.jpg")))
			.andExpect(jsonPath("$.data.created_at", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
