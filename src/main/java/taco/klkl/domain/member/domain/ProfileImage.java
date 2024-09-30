package taco.klkl.domain.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.image.domain.Image;

@Getter
@Entity(name = "profile_image")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "profile_image_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ProfileImage {

	@Id
	@Column(name = "profile_image_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "member_id")
	private Long memberId;

	public abstract Long getImageId();

	public abstract String getUrl();

	public static ProfileImage ofExternal(final Long memberId, final String url) {
		return ExternalProfileImage.of(memberId, url);
	}

	public static ProfileImage ofInternal(final Long memberId, final Image image) {
		return InternalProfileImage.of(memberId, image);
	}

	protected ProfileImage(final Long memberId) {
		this.memberId = memberId;
	}
}
