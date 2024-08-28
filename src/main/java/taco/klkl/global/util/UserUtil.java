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
		return userRepository.findById(1L)
			.orElseThrow(UserNotFoundException::new);
	}

	/**
	 * TODO: 인증정보를 확인해 유저 엔티티를 리턴한다.
	 * 현재 유저 조회
	 * @return
	 */
	public User findCurrentUser() {
		return findTestUser();
	}

	public User findUserById(final Long id) {
		return userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
	}

	public User findUserByName(final String name) {
		return userRepository.findFirstByName(name);
	}

	public String createUsername(final String name, final Long oauthMemberId) {

		String createdName = generateUsername(name, oauthMemberId);

		while (userRepository.existsByName(createdName)) {
			createdName = generateUsername(name, oauthMemberId);
		}

		return createdName;
	}

	private String generateUsername(final String name, final Long oauthMemberId) {

		final Long currentTimeMillis = Instant.now().toEpochMilli();
		final int hashCode = Objects.hash(name, oauthMemberId, currentTimeMillis);

		final String suffix = String.format("%04d", Math.abs(hashCode) % UserConstants.USERNAME_SUFFIX_MOD);

		return name + suffix;
	}

}
