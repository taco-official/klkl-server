package taco.klkl.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
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
	public void testGetCurrentUser() {
		// given
		User user = UserConstants.TEST_USER;
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// when
		UserDetailResponse userDto = userService.getCurrentUser();

		// then
		assertThat(userDto.profileImageUrl()).isEqualTo(user.getProfileImageUrl());
		assertThat(userDto.name()).isEqualTo(user.getName());
		assertThat(userDto.description()).isEqualTo(user.getDescription());
		assertThat(userDto.totalLikeCount()).isEqualTo(UserConstants.DEFAULT_TOTAL_LIKE_COUNT);
	}

	@Test
	@DisplayName("사용자 등록 서비스 테스트")
	public void testCreateUser() {
		// given
		UserCreateRequest requestDto = new UserCreateRequest(
			"이상화",
			"남",
			19,
			"image/ideal-flower.jpg",
			"저는 이상화입니다."
		);
		User user = User.of(
			requestDto.profileImageUrl(),
			requestDto.name(),
			Gender.from(requestDto.description()),
			requestDto.age(),
			requestDto.description()
		);
		when(userRepository.save(any(User.class))).thenReturn(user);

		// when
		UserDetailResponse responseDto = userService.createUser(requestDto);

		// then
		assertThat(responseDto.name()).isEqualTo(requestDto.name());
		assertThat(responseDto.profileImageUrl()).isEqualTo(requestDto.profileImageUrl());
		assertThat(responseDto.description()).isEqualTo(requestDto.description());
		assertThat(responseDto.totalLikeCount()).isEqualTo(UserConstants.DEFAULT_TOTAL_LIKE_COUNT);
	}
}
