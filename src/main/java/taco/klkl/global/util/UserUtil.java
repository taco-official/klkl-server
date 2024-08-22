package taco.klkl.global.util;

import java.time.Instant;
import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.exception.UserNotFoundException;
import taco.klkl.global.common.constants.UserConstants;

@Slf4j
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

	public User findUserById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public User findUserByName(String name) {
		return userRepository.findFirstByName(name);
	}

	public String createUserName(String name, Long id) {

		String createdName = generateUserName(name, id);

		while (userRepository.existsByName(createdName)) {
			createdName = generateUserName(name, id);
		}

		return createdName;
	}

	private String generateUserName(String name, Long id) {

		Long currentTimeMillis = Instant.now().toEpochMilli();
		int hashCode = Objects.hash(name, id, currentTimeMillis);

		String suffix = String.format("%04d", Math.abs(hashCode) % 9973);

		return name + suffix;
	}

}
