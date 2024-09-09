package taco.klkl.domain.user.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.exception.UserNotFoundException;
import taco.klkl.global.util.LikeUtil;
import taco.klkl.global.util.ProductUtil;
import taco.klkl.global.util.UserUtil;

@Slf4j
@Service
@Primary
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final ProductUtil productUtil;
	private final UserUtil userUtil;
	private final LikeUtil likeUtil;

	/**
	 * 임시 나의 정보 조회
	 * name 속성이 "testUser"인 유저를 반환합니다.
	 */
	@Override
	public UserDetailResponse getUserById(final Long id) {
		final User user = userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
		return UserDetailResponse.from(user);
	}

	@Override
	public List<ProductSimpleResponse> getUserProductsById(final Long id) {
		userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
		return productUtil.findProductsByUserId(id).stream()
			.map(ProductSimpleResponse::from)
			.toList();
	}

	@Override
	public List<ProductSimpleResponse> getUserLikesById(final Long id) {
		userRepository.findById(id)
			.orElseThrow(UserNotFoundException::new);
		return likeUtil.findLikesByUserId(id).stream()
			.map(Like::getProduct)
			.map(ProductSimpleResponse::from)
			.toList();
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
