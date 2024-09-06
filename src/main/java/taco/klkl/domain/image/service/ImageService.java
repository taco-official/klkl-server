package taco.klkl.domain.image.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.image.dto.request.MultipleImagesUpdateRequest;
import taco.klkl.domain.image.dto.request.MultipleImagesUploadRequest;
import taco.klkl.domain.image.dto.request.SingleImageUpdateRequest;
import taco.klkl.domain.image.dto.request.SingleImageUploadRequest;
import taco.klkl.domain.image.dto.response.MultipleUploadCompleteResponse;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.image.dto.response.SingleUploadCompleteResponse;

@Service
public interface ImageService {

	PresignedUrlResponse createUserImageUploadUrl(final SingleImageUploadRequest uploadRequest);

	List<PresignedUrlResponse> createProductImageUploadUrls(
		final Long productId,
		final MultipleImagesUploadRequest uploadRequest
	);

	SingleUploadCompleteResponse uploadCompleteUserImage(
		final SingleImageUpdateRequest updateRequest
	);

	MultipleUploadCompleteResponse uploadCompleteProductImages(
		final Long productId,
		final MultipleImagesUpdateRequest updateRequest
	);

}
