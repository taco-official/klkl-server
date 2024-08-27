package taco.klkl.domain.image.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.image.dto.request.ProductImageUploadRequest;
import taco.klkl.domain.image.dto.request.UserImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

@Service
public interface ImageService {

	PresignedUrlResponse createUserImageUploadUrl(final UserImageUploadRequest createRequest);

	void uploadCompleteUserImage();

	PresignedUrlResponse createProductImageUploadUrl(
		final Long productId,
		final ProductImageUploadRequest uploadRequest
	);

	void uploadCompleteProductImage(final Long productId);

}
