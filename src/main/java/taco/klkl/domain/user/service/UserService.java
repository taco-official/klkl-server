package taco.klkl.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequestDto;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;
import taco.klkl.global.common.enums.Gender;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	public static final String MY_NAME = "testUser";

	private final UserRepository userRepository;

	/**
	 * 임시 나의 정보 조회
	 * name 속성이 "testUser"인 유저를 반환합니다.
	 */
	public UserDetailResponseDto getMyInfo() {
		User me = userRepository.findFirstByName(MY_NAME);
		return UserDetailResponseDto.from(me);
	}

	/**
	 *
	 * @param userDto
	 * @return UserDetailResponseDto
	 */
	public UserDetailResponseDto registerUser(UserCreateRequestDto userDto) {
		User user = User.of(
			userDto.profile(),
			userDto.name(),
			Gender.getGenderByDescription(userDto.description()),
			userDto.age(),
			userDto.description()
		);
		userRepository.save(user);
		return UserDetailResponseDto.from(user);
	}

}
