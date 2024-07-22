package taco.klkl.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.enums.Gender;

@SpringBootTest
@Transactional
class UserServiceTest {
	private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	public void beforeEach() {
		userRepository.deleteAll();
	}

	@Test
	public void testMe() {
		// given
		User user = new User(null, "testUser", Gender.MALE, 20, "테스트 유저입니다.");

		// when
		userService.join(user);
		User foundUser = userService.me();

		// then
		assertThat(foundUser.getId()).isNotNull();
		assertThat(user.getName()).isEqualTo(foundUser.getName());
		assertThat(user.getGender()).isEqualTo(foundUser.getGender());
		assertThat(user.getAge()).isEqualTo(foundUser.getAge());
		assertThat(user.getDescription()).isEqualTo(foundUser.getDescription());
	}
}
