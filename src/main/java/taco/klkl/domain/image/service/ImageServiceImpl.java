package taco.klkl.domain.image.service;

import java.time.Duration;
import java.util.List;

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
import taco.klkl.domain.image.domain.UploadState;
import taco.klkl.domain.image.dto.request.ProductImageUploadRequest;
import taco.klkl.domain.image.dto.request.UserImageUploadRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.image.exception.ImageNotFoundException;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.ProductUtil;
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
	private final ProductUtil productUtil;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Value("${cloud.aws.cloudfront.domain}")
	private String cloudFrontDomain;

	@Override
	@Transactional
	public PresignedUrlResponse createUserImageUploadUrl(final UserImageUploadRequest uploadRequest) {
		final ImageType imageType = ImageType.USER_IMAGE;
		final User currentUser = userUtil.findCurrentUser();
		final String imageKey = ImageKeyGenerator.generate();
		final FileExtension fileExtension = FileExtension.from(uploadRequest.fileExtension());

		final Image image = createImageEntity(
			imageType,
			currentUser.getId(),
			imageKey,
			fileExtension
		);
		imageRepository.save(image);

		final PutObjectRequest putObjectRequest = createPutObjectRequest(
			image.createFileName(),
			image.getFileExtension()
		);
		final PutObjectPresignRequest putObjectPresignRequest = createPutObjectPresignRequest(putObjectRequest);

		final PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
		final String presignedUrl = presignedRequest.url().toString();

		return PresignedUrlResponse.from(presignedUrl);
	}

	@Override
	@Transactional
	public void uploadCompleteUserImage() {
		final ImageType imageType = ImageType.USER_IMAGE;
		final User currentUser = userUtil.findCurrentUser();

		final List<Image> images = imageRepository.findAllByImageTypeAndTargetId(imageType, currentUser.getId());

		images.stream()
			.filter(image -> image.getUploadState() == UploadState.COMPLETE)
			.forEach(this::deleteImageEntity);

		final Image newImage = images.stream()
			.filter(image -> image.getUploadState() == UploadState.PENDING)
			.findFirst()
			.orElseThrow(ImageNotFoundException::new);
		newImage.uploadComplete();
		final String imageUrl = createImageUrl(newImage);
		currentUser.updateProfileImageUrl(imageUrl);
	}

	@Override
	@Transactional
	public PresignedUrlResponse createProductImageUploadUrl(
		final Long productId,
		final ProductImageUploadRequest uploadRequest
	) {
		final ImageType imageType = ImageType.PRODUCT_IMAGE;
		final String imageKey = ImageKeyGenerator.generate();
		final FileExtension fileExtension = FileExtension.from(uploadRequest.fileExtension());

		final Image image = createImageEntity(
			imageType,
			productId,
			imageKey,
			fileExtension
		);
		imageRepository.save(image);

		final PutObjectRequest putObjectRequest = createPutObjectRequest(
			image.createFileName(),
			image.getFileExtension()
		);
		final PutObjectPresignRequest putObjectPresignRequest = createPutObjectPresignRequest(putObjectRequest);

		final PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
		final String presignedUrl = presignedRequest.url().toString();

		return PresignedUrlResponse.from(presignedUrl);
	}

	@Override
	@Transactional
	public void uploadCompleteProductImage(final Long productId) {
		final ImageType imageType = ImageType.PRODUCT_IMAGE;

		final List<Image> images = imageRepository.findAllByImageTypeAndTargetId(imageType, productId);

		images.stream()
			.filter(image -> image.getUploadState() == UploadState.COMPLETE)
			.forEach(this::deleteImageEntity);

		final List<Image> newImages = images.stream()
			.filter(image -> image.getUploadState() == UploadState.PENDING)
			.toList();
		newImages.forEach(Image::uploadComplete);
		final List<String> imageUrls = newImages.stream()
			.map(this::createImageUrl)
			.toList();
		Product product = productUtil.findProductEntityById(productId);
		product.updateImages(imageUrls);
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
		final Long targetId,
		final String imageKey,
		final FileExtension fileExtension
	) {
		return Image.of(imageType, targetId, imageKey, fileExtension);
	}

	private void deleteImageEntity(final Image image) {
		imageRepository.delete(image);
	}

	private String createImageUrl(final Image image) {
		return "https://" + cloudFrontDomain + "/" + image.createFileName();
	}
}
