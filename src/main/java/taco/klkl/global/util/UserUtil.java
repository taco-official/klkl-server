package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.service.UserService;

@Component
@RequiredArgsConstructor
public class UserUtil {

	private final UserRepository userRepository;

	public User findTestUser() {
		return userRepository.findFirstByName(UserService.MY_NAME);
	}
}
