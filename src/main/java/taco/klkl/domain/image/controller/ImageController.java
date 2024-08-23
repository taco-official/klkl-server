package taco.klkl.domain.image.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.dto.request.UserProfileUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.image.service.ImageService;

@RestController
@Tag(name = "10. 이미지", description = "이미지 관련 API")
@RequiredArgsConstructor
public class ImageController {

	public final ImageService imageService;

	@Operation(
		summary = "유저 프로필 이미지 업로드용 Presigned URL 생성",
		description = "유저 프로필 이미지 업로드용 Presigned URL를 생성합니다."
	)
	@PostMapping("/v1/users/me/upload-url")
	public PresignedUrlResponse createUserProfileUploadPresignedUrl(
		@Valid @RequestBody UserProfileUploadRequest request
	) {
		return imageService.createUserProfileUploadPresignedUrl(request);
	}
}
