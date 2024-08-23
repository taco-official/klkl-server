package taco.klkl.domain.image.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import taco.klkl.domain.image.dao.ImageRepository;
import taco.klkl.domain.image.domain.FileExtension;
import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.image.domain.ImageType;
import taco.klkl.domain.image.dto.request.UserProfileImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private static final Duration SIGNATURE_DURATION = Duration.ofMinutes(5);
	private static final ObjectCannedACL REQUEST_ACL = ObjectCannedACL.PRIVATE;

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	private final ImageRepository imageRepository;

	private final UserUtil userUtil;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	@Transactional
	public PresignedUrlResponse generateProfileImageUploadUrl(final UserProfileImageUploadRequest uploadRequest) {
		final User currentUser = userUtil.findCurrentUser();
		final String imageKey = generateImageKey();
		final FileExtension fileExtension = FileExtension.from(uploadRequest.fileExtension());
		final String fileName = createFileName(
			ImageType.USER_PROFILE,
			currentUser.getId(),
			imageKey,
			fileExtension
		);

		final PutObjectRequest putObjectRequest = createPutObjectRequest(
			fileName,
			fileExtension
		);
		final PutObjectPresignRequest putObjectPresignRequest = createPutObjectPresignRequest(putObjectRequest);

		final PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
		final String presignedUrl = presignedRequest.url().toString();

		final Image image = createImageEntity(
			ImageType.USER_PROFILE,
			imageKey,
			fileExtension
		);
		imageRepository.save(image);

		return PresignedUrlResponse.from(presignedUrl);
	}

	private String generateImageKey() {
		return UUID.randomUUID().toString();
	}

	private String createFileName(
		final ImageType imageType,
		final Long currentUserId,
		final String imageKey,
		final FileExtension fileExtension
	) {
		StringBuilder sb = new StringBuilder();
		sb.append(imageType.getValue()).append("/")
			.append(currentUserId).append("/")
			.append(imageKey).append(".")
			.append(fileExtension.getValue());
		return sb.toString();
	}

	private PutObjectRequest createPutObjectRequest(
		final String fileName,
		final FileExtension fileExtension
	) {
		return PutObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.contentType("image/" + fileExtension.getValue())
			.acl(REQUEST_ACL)
			.build();
	}

	private PutObjectPresignRequest createPutObjectPresignRequest(
		final PutObjectRequest putObjectRequest
	) {
		return PutObjectPresignRequest.builder()
			.signatureDuration(SIGNATURE_DURATION)
			.putObjectRequest(putObjectRequest)
			.build();
	}

	private Image createImageEntity(
		final ImageType imageType,
		final String imageKey,
		final FileExtension fileExtension
	) {
		return Image.of(imageType, imageKey, fileExtension);
	}
}
