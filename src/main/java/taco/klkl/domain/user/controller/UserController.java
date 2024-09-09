package taco.klkl.domain.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import taco.klkl.domain.user.dto.request.UserUpdateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.service.UserService;
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
	public List<ProductSimpleResponse> getMyProducts() {
		final User me = userUtil.getCurrentUser();
		return userService.getUserProductsById(me.getId());
	}

	@Operation(summary = "유저의 상품 목록 조회", description = "유저의 상품 목록을 조회합니다.")
	@GetMapping("/{userId}/products")
	public List<ProductSimpleResponse> getUserProducts(@PathVariable final Long userId) {
		return userService.getUserProductsById(userId);
	}

	@Operation(summary = "내 종아요 목록 조회", description = "내 좋아요 목록을 조회합니다.")
	@GetMapping("/me/likes")
	public List<ProductSimpleResponse> getMyLikes() {
		final User me = userUtil.getCurrentUser();
		return userService.getUserLikesById(me.getId());
	}

	@Operation(summary = "내 정보 수정", description = "내 정보를 수정합니다.")
	@PutMapping("/me")
	public UserDetailResponse updateMe(@Valid @RequestBody final UserUpdateRequest request) {
		return userService.updateUser(request);
	}
}
