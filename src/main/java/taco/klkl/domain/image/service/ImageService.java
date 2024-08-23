package taco.klkl.domain.image.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.image.dto.request.UserProfileUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

@Service
public interface ImageService {

	PresignedUrlResponse createUserProfileUploadPresignedUrl(final UserProfileUploadRequest createRequest);
}
