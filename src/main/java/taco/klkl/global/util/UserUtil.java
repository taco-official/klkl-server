package taco.klkl.global.util;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.exception.UserNotFoundException;
import taco.klkl.global.common.constants.UserConstants;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserUtil {

	private final UserRepository userRepository;

	/**
	 * TODO: 인증정보를 확인해 유저 엔티티를 리턴한다.
	 * 현재 유저 조회
	 * @return
	 */
	public User getCurrentUser() {
		return getTestUser();
	}

	public String createUsername(final String name, final Long oauthMemberId) {

		String createdName = generateUsername(name, oauthMemberId);

		while (userRepository.existsByName(createdName)) {
			createdName = generateUsername(name, oauthMemberId);
		}

		return createdName;
	}

	public static String generateProfileUrlByUser(final User user) {
		return Optional.ofNullable(user.getImage())
			.map(Image::getUrl)
			.orElse(null);
	}

	private User getTestUser() {
		return userRepository.findById(1L)
			.orElseThrow(UserNotFoundException::new);
	}

	private String generateUsername(final String name, final Long oauthMemberId) {

		final Long currentTimeMillis = Instant.now().toEpochMilli();
		final int hashCode = Objects.hash(name, oauthMemberId, currentTimeMillis);

		final String suffix = String.format("%04d", Math.abs(hashCode) % UserConstants.USERNAME_SUFFIX_MOD);

		return name + suffix;
	}

}
