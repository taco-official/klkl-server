package taco.klkl.domain.image.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import taco.klkl.domain.image.dto.request.ImageCreateRequest;
import taco.klkl.domain.image.dto.response.PresignedUrlResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	private final ImageRepository imageRepository;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	public PresignedUrlResponse createImagePresignedUrl(final ImageCreateRequest createRequest) {
		final String imageUUID = generateUUID();
		final String fileName = createFileName(imageUUID, createRequest.fileExtension());

		final PutObjectRequest objectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.contentType("image/" + createRequest.fileExtension().getValue())
			.acl(ObjectCannedACL.PUBLIC_READ)
			.build();

		final PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
			.signatureDuration(Duration.ofMinutes(30))
			.putObjectRequest(objectRequest)
			.build();

		final PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
		final String presignedUrl = presignedRequest.url().toString();

		final Image image = createImageEntity(imageUUID, createRequest.fileExtension());
		imageRepository.save(image);

		System.out.println("Generated Presigned URL: " + presignedUrl);
		System.out.println("Bucket: " + bucketName);
		System.out.println("Key: " + fileName);
		System.out.println("Content-Type: image/" + createRequest.fileExtension().getValue());
		return PresignedUrlResponse.from(presignedUrl);
	}

	private String generateUUID() {
		return UUID.randomUUID().toString();
	}

	private String createFileName(
		String imageUUID,
		FileExtension fileExtension
	) {
		StringBuilder sb = new StringBuilder();
		sb.append(imageUUID)
			.append(".")
			.append(fileExtension.getValue());
		return sb.toString();
	}

	private Image createImageEntity(final String imageUUID, final FileExtension fileExtension) {
		return Image.of(imageUUID, fileExtension);
	}
}
