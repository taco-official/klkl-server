package taco.klkl.domain.image.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.image.dto.request.UserImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.image.service.ImageService;

@Slf4j
@RestController
@Tag(name = "0. 이미지", description = "이미지 관련 API")
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@Operation(
		summary = "유저 이미지 업로드를 위한 Presigned URL 생성",
		description = "유저 이미지 업로드를 위한 Presigned URL를 생성합니다."
	)
	@PostMapping("/me/image/upload-url")
	public PresignedUrlResponse createUserImageUploadUrl(
		@Valid @RequestBody UserImageUploadRequest request
	) {
		return imageService.createUserImageUploadUrl(request);
	}
}
