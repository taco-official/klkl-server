package taco.klkl.domain.image.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.image.dto.request.UserProfileImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

@Service
public interface ImageService {

	PresignedUrlResponse generateProfileImageUploadUrl(final UserProfileImageUploadRequest createRequest);
}
