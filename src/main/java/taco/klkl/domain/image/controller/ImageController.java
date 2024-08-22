package taco.klkl.domain.image.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.dto.request.ImageCreateRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.image.service.ImageService;

@RestController
@Tag(name = "10. 이미지", description = "이미지 관련 API")
@RequiredArgsConstructor
public class ImageController {

	public final ImageService imageService;

	@Operation(summary = "이미지 Presigned URL 생성", description = "이미지 Presigned URL를 생성합니다.")
	@PostMapping("/v1/images/upload-url")
	public PresignedUrlResponse createImagePresignedUrl(
		@Valid @RequestBody ImageCreateRequest request
	) {
		return imageService.createImagePresignedUrl(request);
	}
}
