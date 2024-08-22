package taco.klkl.domain.image.domain;

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
	@Column(name = "image_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "image_uuid")
	private String imageUuid;

	@Enumerated(EnumType.STRING)
	@Column(name = "file_extension")
	private FileExtension fileExtension;

	private Image(
		final String imageUuid,
		final FileExtension fileExtension
	) {
		this.imageUuid = imageUuid;
		this.fileExtension = fileExtension;
	}

	public static Image of(
		final String imageUuid,
		final FileExtension fileExtension
	) {
		return new Image(imageUuid, fileExtension);
	}
}
