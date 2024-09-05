package taco.klkl.domain.image.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.image.dto.request.MultipleImagesUpdateRequest;
import taco.klkl.domain.image.dto.request.MultipleImagesUploadRequest;
import taco.klkl.domain.image.dto.request.SingleImageUpdateRequest;
import taco.klkl.domain.image.dto.request.SingleImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

@Service
public interface ImageService {

	PresignedUrlResponse createUserImageUploadUrl(final SingleImageUploadRequest uploadRequest);

	List<PresignedUrlResponse> createProductImageUploadUrls(
		final Long productId,
		final MultipleImagesUploadRequest uploadRequest
	);

	void uploadCompleteUserImage(
		final SingleImageUpdateRequest updateRequest
	);

	void uploadCompleteProductImages(
		final Long productId,
		final MultipleImagesUpdateRequest updateRequest
	);

}
