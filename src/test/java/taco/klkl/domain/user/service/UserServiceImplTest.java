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
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.common.constants.UserConstants;
import taco.klkl.global.util.UserUtil;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImplTest.class);

	@Mock
	UserRepository userRepository;

	@Mock
	UserUtil userUtil;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Test
	@DisplayName("내 정보 조회 서비스 테스트")
	public void testGetUserById() {
		// given

		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);
		when(user.getName()).thenReturn("testUser");
		when(user.getDescription()).thenReturn("테스트입니다.");
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		// when
		UserDetailResponse userDto = userServiceImpl.getUserById(1L);

		// then
		assertThat(userDto.id()).isEqualTo(user.getId());
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
			"저는 이상화입니다."
		);
		User user = User.of(
			requestDto.name(),
			requestDto.description()
		);
		when(userRepository.save(any(User.class))).thenReturn(user);

		// when
		User user1 = userServiceImpl.createUser(requestDto);
		UserDetailResponse responseDto = UserDetailResponse.from(user1);

		// then
		assertThat(responseDto.name()).isEqualTo(requestDto.name());
		assertThat(responseDto.description()).isEqualTo(requestDto.description());
		assertThat(responseDto.totalLikeCount()).isEqualTo(UserConstants.DEFAULT_TOTAL_LIKE_COUNT);
	}
}
