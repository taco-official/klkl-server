package taco.klkl.domain.member.domain;

import static taco.klkl.global.common.constants.MemberConstants.DEFAULT_DESCRIPTION;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.member.domain.profile.ProfileImage;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(columnDefinition = "BINARY(16)", unique = true, nullable = false, updatable = false)
	private UUID uuid;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "profile_image_id")
	private ProfileImage profileImage;

	@Column(length = 30, nullable = false, unique = true)
	private String handle;

	@Column(length = 30, nullable = false)
	private String displayName;

	@Column(length = 100)
	private String description;

	private String provider;

	private String providerId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public static Member ofUser(
		final String displayName,
		final String provider,
		final String providerId
	) {
		return new Member(displayName, provider, providerId, Role.USER);
	}

	public String getMemberKey() {
		return uuid.toString();
	}

	public void update(
		final String displayName,
		final String description
	) {
		this.displayName = displayName;
		this.description = description;
	}

	public void updateProfileImage(final String url) {
		this.profileImage = ProfileImage.ofExternal(this.id, url);
	}

	public void updateProfileImage(final Image image) {
		this.profileImage = ProfileImage.ofInternal(this.id, image);
	}

	private Member(
		final String displayName,
		final String provider,
		final String providerId,
		final Role role
	) {
		this.uuid = UUID.randomUUID();
		this.handle = HandleGenerator.generate();
		this.displayName = displayName;
		this.description = DEFAULT_DESCRIPTION;
		this.provider = provider;
		this.providerId = providerId;
		this.role = role;
		this.createdAt = LocalDateTime.now();
	}
}
