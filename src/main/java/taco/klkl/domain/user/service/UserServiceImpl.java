package taco.klkl.domain.user.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.util.UserUtil;

@Slf4j
@Service
@Primary
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final UserUtil userUtil;

	/**
	 * 임시 나의 정보 조회
	 * name 속성이 "testUser"인 유저를 반환합니다.
	 */
	@Override
	public UserDetailResponse getCurrentUser() {
		final User currentUser = userUtil.getCurrentUser();
		return UserDetailResponse.from(currentUser);
	}

	@Override
	@Transactional
	public UserDetailResponse updateUser(final UserUpdateRequest updateRequest) {
		User user = userUtil.getCurrentUser();
		updateUserEntity(user, updateRequest);
		return UserDetailResponse.from(user);
	}

	@Override
	@Transactional
	public User createUser(final UserCreateRequest createRequest) {
		final User user = createUserEntity(createRequest);
		return userRepository.save(user);
	}

	private User createUserEntity(final UserCreateRequest createRequest) {
		final String name = createRequest.name();
		final String description = createRequest.description();

		return User.of(
			name,
			description
		);
	}

	private void updateUserEntity(final User user, final UserUpdateRequest updateRequest) {
		final String name = updateRequest.name();
		final String description = updateRequest.description();

		user.update(
			name,
			description
		);
	}
}
