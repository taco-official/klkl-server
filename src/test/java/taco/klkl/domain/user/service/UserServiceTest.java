package taco.klkl.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.Gender;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequestDto;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;
import taco.klkl.global.common.constants.UserConstants;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserService userService;

	@Test
	@DisplayName("내 정보 조회 서비스 테스트")
	public void testGetMyInfo() {
		// given
		User user = UserConstants.TEST_USER;
		when(userRepository.findFirstByName(UserConstants.TEST_USER_NAME)).thenReturn(user);

		// when
		UserDetailResponseDto userDto = userService.getMyInfo();

		// then
		assertThat(userDto.profile()).isEqualTo(user.getProfile());
		assertThat(userDto.name()).isEqualTo(user.getName());
		assertThat(userDto.description()).isEqualTo(user.getDescription());
		assertThat(userDto.totalLikeCount()).isEqualTo(UserConstants.DEFAULT_TOTAL_LIKE_COUNT);
	}

	@Test
	@DisplayName("사용자 등록 서비스 테스트")
	public void testRegisterUser() {
		// given
		UserCreateRequestDto requestDto = new UserCreateRequestDto(
			"이상화",
			"남",
			19,
			"image/ideal-flower.jpg",
			"저는 이상화입니다."
		);
		User user = User.of(
			requestDto.profile(),
			requestDto.name(),
			Gender.getGenderByDescription(requestDto.description()),
			requestDto.age(),
			requestDto.description()
		);
		when(userRepository.save(any(User.class))).thenReturn(user);

		// when
		UserDetailResponseDto responseDto = userService.registerUser(requestDto);

		// then
		assertThat(responseDto.name()).isEqualTo(requestDto.name());
		assertThat(responseDto.profile()).isEqualTo(requestDto.profile());
		assertThat(responseDto.description()).isEqualTo(requestDto.description());
		assertThat(responseDto.totalLikeCount()).isEqualTo(UserConstants.DEFAULT_TOTAL_LIKE_COUNT);
	}
}
