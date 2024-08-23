package taco.klkl.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.image.dto.request.UserProfileImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.image.service.ImageService;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.service.UserService;

@Slf4j
@RestController
@Tag(name = "1. 유저", description = "유저 관련 API")
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final ImageService imageService;

	@Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다. (테스트용)")
	@GetMapping("/me")
	public ResponseEntity<UserDetailResponse> getMe() {
		UserDetailResponse userDto = userService.getMyInfo();
		return ResponseEntity.ok().body(userDto);
	}

	@Operation(
		summary = "프로필 이미지 업로드를 위한 Presigned URL 생성",
		description = "프로필 이미지 업로드를 위한 Presigned URL를 생성합니다."
	)
	@PostMapping("/me/profile-image/upload-url")
	public PresignedUrlResponse createProfileImageUploadUrl(
		@Valid @RequestBody UserProfileImageUploadRequest request
	) {
		return imageService.generateProfileImageUploadUrl(request);
	}
}
