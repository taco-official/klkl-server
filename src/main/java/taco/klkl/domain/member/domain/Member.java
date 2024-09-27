package taco.klkl.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.image.domain.Image;

@Getter
@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"name", "tag"})
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "profile_image_id")
	private ProfileImage profileImage;

	@Column(length = 30, nullable = false)
	private String name;

	@Column(length = 4, nullable = false)
	private String tag;

	@Column(length = 100)
	private String description;

	private String provider;

	private String providerId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private Member(
		final String name,
		final String tag,
		final String provider,
		final String providerId,
		final Role role
	) {
		this.name = name;
		this.tag = tag;
		this.description = "";
		this.provider = provider;
		this.providerId = providerId;
		this.role = role;
		this.createdAt = LocalDateTime.now();
	}

	public static Member ofUser(
		final String name,
		final String tag,
		final String provider,
		final String providerId
	) {
		return new Member(name, tag, provider, providerId, Role.USER);
	}

	public String getMemberKey() {
		return name;
	}

	public void update(
		final String name,
		final String description
	) {
		this.name = name;
		this.description = description;
	}

	public void updateProfileImage(final String url) {
		this.profileImage = ProfileImage.ofExternal(this.id, url);
	}

	public void updateProfileImage(final Image image) {
		this.profileImage = ProfileImage.ofInternal(this.id, image);
	}
}
