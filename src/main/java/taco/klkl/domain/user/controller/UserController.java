package taco.klkl.domain.user.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.product.dto.response.ProductSimpleResponse;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserFollowRequest;
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.dto.response.UserFollowResponse;
import taco.klkl.domain.user.dto.response.UserSimpleResponse;
import taco.klkl.domain.user.service.UserService;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.response.PagedResponse;
import taco.klkl.global.util.UserUtil;

@Slf4j
@RestController
@Tag(name = "1. 유저", description = "유저 관련 API")
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserUtil userUtil;

	@Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
	@GetMapping("/me")
	public UserDetailResponse getMe() {
		final User me = userUtil.getCurrentUser();
		return userService.getUserById(me.getId());
	}

	@Operation(summary = "유저 정보 조회", description = "유저 정보를 조회합니다.")
	@GetMapping("/{userId}")
	public UserDetailResponse getUser(@PathVariable final Long userId) {
		return userService.getUserById(userId);
	}

	@Operation(summary = "내 상품 목록 조회", description = "내 상품 목록을 조회합니다.")
	@GetMapping("/me/products")
	public PagedResponse<ProductSimpleResponse> getMyProducts(
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable
	) {
		final User me = userUtil.getCurrentUser();
		return userService.getUserProductsById(me.getId(), pageable);
	}

	@Operation(summary = "유저의 상품 목록 조회", description = "유저의 상품 목록을 조회합니다.")
	@GetMapping("/{userId}/products")
	public PagedResponse<ProductSimpleResponse> getUserProducts(
		@PathVariable final Long userId,
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable
	) {
		return userService.getUserProductsById(userId, pageable);
	}

	@Operation(summary = "내 종아요 목록 조회", description = "내 좋아요 목록을 조회합니다.")
	@GetMapping("/me/likes")
	public PagedResponse<ProductSimpleResponse> getMyLikes(
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable
	) {
		final User me = userUtil.getCurrentUser();
		return userService.getUserLikesById(me.getId(), pageable);
	}

	@Operation(summary = "내 팔로잉 목록 조회", description = "내 팔로잉 목록을 조회합니다.")
	@GetMapping("/following")
	public List<UserSimpleResponse> getMyFollowing() {
		final User me = userUtil.getCurrentUser();
		return userService.getUserFollowingById(me.getId());
	}

	@Operation(summary = "유저 팔로우", description = "유저를 팔로우합니다.")
	@PostMapping("/following")
	public UserFollowResponse followUser(@Valid @RequestBody final UserFollowRequest request) {
		return userService.createUserFollow(request);
	}

	@Operation(summary = "내 정보 수정", description = "내 정보를 수정합니다.")
	@PutMapping("/me")
	public UserDetailResponse updateMe(@Valid @RequestBody final UserUpdateRequest request) {
		return userService.updateUser(request);
	}
}
