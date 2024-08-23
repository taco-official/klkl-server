package taco.klkl.domain.image.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
	@Id
	@Column(name = "image_id",
		nullable = false
	)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(
		name = "image_type",
		nullable = false
	)
	private ImageType imageType;

	@Column(
		name = "image_key",
		nullable = false
	)
	private String imageKey;

	@Enumerated(EnumType.STRING)
	@Column(
		name = "file_extension",
		nullable = false
	)
	private FileExtension fileExtension;

	@Column(
		name = "created_at",
		nullable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

	private Image(
		final ImageType imageType,
		final String imageKey,
		final FileExtension fileExtension
	) {
		this.imageType = imageType;
		this.imageKey = imageKey;
		this.fileExtension = fileExtension;
		this.createdAt = LocalDateTime.now();
	}

	public static Image of(
		final ImageType imageType,
		final String imageUuid,
		final FileExtension fileExtension
	) {
		return new Image(imageType, imageUuid, fileExtension);
	}
}
