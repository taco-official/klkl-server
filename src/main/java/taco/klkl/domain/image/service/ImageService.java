package taco.klkl.domain.image.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.image.dto.request.ImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

@Service
public interface ImageService {

	PresignedUrlResponse createUserImageUploadUrl(final ImageUploadRequest createRequest);

	void uploadCompleteUserImage();

	PresignedUrlResponse createProductImageUploadUrl(
		final Long productId,
		final ImageUploadRequest uploadRequest
	);

	void uploadCompleteProductImage(final Long productId);

}
