package taco.klkl.domain.member.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("EXTERNAL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalProfileImage extends ProfileImage {

	@Column(name = "url")
	private String url;

	public static ExternalProfileImage of(final Long memberId, final String url) {
		return new ExternalProfileImage(memberId, url);
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Long getImageId() {
		return 0L;
	}

	private ExternalProfileImage(final Long memberId, final String url) {
		super(memberId);
		this.url = url;
	}
}
