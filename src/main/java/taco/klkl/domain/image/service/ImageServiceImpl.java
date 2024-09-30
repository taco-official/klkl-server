package taco.klkl.domain.image.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import taco.klkl.domain.image.dto.request.MultipleImagesUpdateRequest;
import taco.klkl.domain.image.dto.request.MultipleImagesUploadRequest;
import taco.klkl.domain.image.dto.request.SingleImageUpdateRequest;
import taco.klkl.domain.image.dto.request.SingleImageUploadRequest;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductImage;
import taco.klkl.global.util.ImageUtil;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ProductUtil;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private static final Duration SIGNATURE_DURATION = Duration.ofMinutes(5);
	private static final ObjectCannedACL REQUEST_ACL = ObjectCannedACL.PRIVATE;

	private final S3Presigner s3Presigner;
	private final ImageRepository imageRepository;
	private final MemberUtil memberUtil;
	private final ProductUtil productUtil;
	private final ImageUtil imageUtil;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	@Transactional
	public PresignedUrlResponse createUserImageUploadUrl(final SingleImageUploadRequest uploadRequest) {
		final Member currentMember = memberUtil.getCurrentMember();
		final FileExtension fileExtension = FileExtension.from(uploadRequest.fileExtension());
		return createImageUploadUrl(ImageType.MEMBER_IMAGE, currentMember.getId(), fileExtension);
	}

	@Override
	@Transactional
	public List<PresignedUrlResponse> createProductImageUploadUrls(
		final Long productId,
		final MultipleImagesUploadRequest uploadRequest
	) {
		return uploadRequest.fileExtensions().stream()
			.map(FileExtension::from)
			.map(fileExtension -> createImageUploadUrl(ImageType.PRODUCT_IMAGE, productId, fileExtension))
			.toList();
	}

	@Override
	@Transactional
	public ImageResponse uploadCompleteUserImage(
		final SingleImageUpdateRequest updateRequest
	) {
		final Member currentMember = memberUtil.getCurrentMember();
		expireOldImages(ImageType.MEMBER_IMAGE, currentMember.getId());

		Image updatedImage = imageUtil.findImageEntityByImageTypeAndId(ImageType.MEMBER_IMAGE, updateRequest.imageId());
		updatedImage.markAsComplete();

		currentMember.updateProfileImage(updatedImage);

		return ImageResponse.from(currentMember.getProfileImage());
	}

	@Override
	@Transactional
	public List<ImageResponse> uploadCompleteProductImages(
		final Long productId,
		final MultipleImagesUpdateRequest updateRequest
	) {
		expireOldImages(ImageType.PRODUCT_IMAGE, productId);

		List<Image> updatedImages = updateRequest.imageIds().stream()
			.map(imageId -> imageUtil.findImageEntityByImageTypeAndId(ImageType.PRODUCT_IMAGE, imageId))
			.toList();

		updatedImages.forEach(Image::markAsComplete);

		Product product = productUtil.findProductEntityById(productId);
		product.updateImages(updatedImages);

		return product.getImages().stream()
			.map(ProductImage::getImage)
			.map(ImageResponse::from)
			.toList();
	}

	private PresignedUrlResponse createImageUploadUrl(
		final ImageType imageType,
		final Long targetId,
		final FileExtension fileExtension
	) {
		final String imageKey = ImageKeyGenerator.generate();

		final Image image = createAndSaveImageEntity(imageType, targetId, imageKey, fileExtension);

		final PutObjectRequest putObjectRequest
			= createPutObjectRequest(image.generateFileName(), image.getFileExtension());
		final PutObjectPresignRequest putObjectPresignRequest = createPutObjectPresignRequest(putObjectRequest);

		final PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
		final String presignedUrl = presignedRequest.url().toString();

		return PresignedUrlResponse.of(image, presignedUrl);
	}

	private Image createAndSaveImageEntity(
		final ImageType imageType,
		final Long targetId,
		final String imageKey,
		final FileExtension fileExtension
	) {
		final Image image = Image.of(imageType, targetId, imageKey, fileExtension);
		return imageRepository.save(image);
	}

	private void expireOldImages(final ImageType imageType, Long targetId) {
		imageRepository.findAllByImageTypeAndTargetId(imageType, targetId).stream()
			.filter(image -> image.getUploadState() == UploadState.COMPLETE)
			.forEach(Image::markAsOutdated);
	}

	private PutObjectRequest createPutObjectRequest(final String fileName, final FileExtension fileExtension) {
		return PutObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.contentType("image/" + fileExtension.getValue())
			.acl(REQUEST_ACL)
			.build();
	}

	private PutObjectPresignRequest createPutObjectPresignRequest(final PutObjectRequest putObjectRequest) {
		return PutObjectPresignRequest.builder()
			.signatureDuration(SIGNATURE_DURATION)
			.putObjectRequest(putObjectRequest)
			.build();
	}
}
