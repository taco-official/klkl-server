package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.UserConstants;

@Component
@RequiredArgsConstructor
public class UserUtil {

	private final UserRepository userRepository;

	public User findTestUser() {
		return userRepository.findFirstByName(UserConstants.TEST_USER_NAME);
	}

	/**
	 * TODO: 인증정보를 확인해 유저 엔티티를 리턴한다.
	 * 현재 유저 조회
	 * @return
	 */
	public User findCurrentUser() {
		return userRepository.findFirstByName(UserConstants.TEST_USER_NAME);
	}
}
