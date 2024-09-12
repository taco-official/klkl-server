package taco.klkl.domain.user.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.user.dao.FollowRepository;
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.Follow;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.FollowResponse;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.dto.response.UserSimpleResponse;
import taco.klkl.domain.user.exception.SelfFollowNotAllowedException;
import taco.klkl.domain.user.exception.UserNotFoundException;
import taco.klkl.global.common.response.PagedResponse;
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
	private final FollowRepository followRepository;

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
	public PagedResponse<ProductSimpleResponse> getUserProductsById(final Long id, final Pageable pageable) {
		validateUser(id);
		final Pageable sortedPageable = createPageableSortedByCreatedAtDesc(pageable);
		final Page<Product> userProducts = productUtil.findProductsByUserId(id, sortedPageable);
		return PagedResponse.of(userProducts, ProductSimpleResponse::from);
	}

	@Override
	public PagedResponse<ProductSimpleResponse> getUserLikesById(final Long id, final Pageable pageable) {
		validateUser(id);
		final Pageable sortedPageable = createPageableSortedByCreatedAtDesc(pageable);
		final Page<Like> likes = likeUtil.findLikesByUserId(id, sortedPageable);
		final Page<Product> likedProducts = likes.map(Like::getProduct);
		return PagedResponse.of(likedProducts, ProductSimpleResponse::from);
	}

	@Override
	public List<UserSimpleResponse> getFollowings() {
		final User follower = userUtil.getCurrentUser();
		return followRepository.findByFollowerId(follower.getId()).stream()
			.map(Follow::getFollowing)
			.map(UserSimpleResponse::from)
			.toList();
	}

	@Override
	public FollowResponse getFollowingStatus(final Long followingId) {
		final User follower = userUtil.getCurrentUser();
		final User following = userRepository.findById(followingId)
			.orElseThrow(UserNotFoundException::new);
		final boolean isFollowing = followRepository.existsByFollowerAndFollowing(follower, following);
		return FollowResponse.of(isFollowing, follower, following);
	}

	@Override
	@Transactional
	public User createUser(final UserCreateRequest createRequest) {
		final User user = createUserEntity(createRequest);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public FollowResponse createFollow(final Long followingId) {
		final User follower = userUtil.getCurrentUser();
		final User following = userRepository.findById(followingId)
			.orElseThrow(UserNotFoundException::new);
		validateNotMe(follower, following);
		if (isFollowPresent(follower, following)) {
			return FollowResponse.of(true, follower, following);
		}
		final Follow follow = Follow.of(follower, following);
		followRepository.save(follow);
		return FollowResponse.of(true, follower, following);
	}

	@Override
	@Transactional
	public FollowResponse removeFollow(final Long followingId) {
		final User follower = userUtil.getCurrentUser();
		final User following = userRepository.findById(followingId)
			.orElseThrow(UserNotFoundException::new);
		if (isFollowPresent(follower, following)) {
			followRepository.deleteByFollowerAndFollowing(follower, following);
		}
		return FollowResponse.of(false, follower, following);
	}

	@Override
	@Transactional
	public UserDetailResponse updateUser(final UserUpdateRequest updateRequest) {
		User user = userUtil.getCurrentUser();
		updateUserEntity(user, updateRequest);
		return UserDetailResponse.from(user);
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

	private Pageable createPageableSortedByCreatedAtDesc(final Pageable pageable) {
		return PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(Sort.Direction.DESC, "createdAt")
		);
	}

	private boolean isFollowPresent(final User follower, final User following) {
		return followRepository.existsByFollowerAndFollowing(follower, following);
	}

	private void validateUser(final Long id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException();
		}
	}

	private void validateNotMe(final User follower, final User following) {
		if (follower.equals(following)) {
			throw new SelfFollowNotAllowedException();
		}
	}
}
