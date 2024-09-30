package taco.klkl.domain.image.service;

import java.util.List;

import taco.klkl.domain.image.dto.request.MultipleImagesUpdateRequest;
import taco.klkl.domain.image.dto.request.MultipleImagesUploadRequest;
import taco.klkl.domain.image.dto.request.SingleImageUpdateRequest;
import taco.klkl.domain.image.dto.request.SingleImageUploadRequest;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

public interface ImageService {

	PresignedUrlResponse createUserImageUploadUrl(final SingleImageUploadRequest uploadRequest);

	List<PresignedUrlResponse> createProductImageUploadUrls(
		final Long productId,
		final MultipleImagesUploadRequest uploadRequest
	);

	ImageResponse uploadCompleteUserImage(final SingleImageUpdateRequest updateRequest);

	List<ImageResponse> uploadCompleteProductImages(
		final Long productId,
		final MultipleImagesUpdateRequest updateRequest
	);

}
