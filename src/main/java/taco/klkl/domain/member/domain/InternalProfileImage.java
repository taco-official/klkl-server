package taco.klkl.domain.member.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.image.domain.Image;

@Getter
@Entity
@DiscriminatorValue("INTERNAL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InternalProfileImage extends ProfileImage {

	@OneToOne
	@JoinColumn(name = "image_id")
	private Image image;

	public static InternalProfileImage of(final Long memberId, final Image image) {
		return new InternalProfileImage(memberId, image);
	}

	@Override
	public Long getImageId() {
		return image.getId();
	}

	@Override
	public String getUrl() {
		return image.getUrl();
	}

	private InternalProfileImage(final Long memberId, final Image image) {
		super(memberId);
		this.image = image;
	}
}
