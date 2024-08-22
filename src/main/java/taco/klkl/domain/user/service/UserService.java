package taco.klkl.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.Gender;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.common.constants.UserConstants;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	/**
	 * 임시 나의 정보 조회
	 * name 속성이 "testUser"인 유저를 반환합니다.
	 */
	public UserDetailResponse getMyInfo() {
		User me = userRepository.findFirstByName(UserConstants.TEST_USER_NAME);
		return UserDetailResponse.from(me);
	}

	/**
	 *
	 * @param userDto
	 * @return User
	 */
	public User createUser(UserCreateRequest userDto) {
		final User user = User.of(
			userDto.profile(),
			userDto.name(),
			Gender.getGenderByDescription(userDto.description()),
			userDto.age(),
			userDto.description()
		);

		return userRepository.save(user);
	}

}
