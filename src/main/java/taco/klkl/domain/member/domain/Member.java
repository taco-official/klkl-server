package taco.klkl.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.image.domain.Image;

@Getter
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "member_id")
	private Long id;

	@OneToOne(
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@JoinColumn(name = "profile_image_id")
	private ProfileImage profileImage;

	@Column(
		name = "name",
		unique = true,
		length = 30,
		nullable = false
	)
	private String name;

	@Column(
		name = "description",
		length = 100
	)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(
		name = "role",
		nullable = false
	)
	private Role role;

	@Column(
		name = "created_at",
		nullable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

	private Member(final String name) {
		this.name = name;
		this.description = "";
		this.role = Role.USER;
		this.createdAt = LocalDateTime.now();
	}

	public static Member of(final String name) {
		return new Member(name);
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
