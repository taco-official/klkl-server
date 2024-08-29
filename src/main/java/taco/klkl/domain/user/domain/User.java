package taco.klkl.domain.user.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.global.common.constants.UserConstants;

@Getter
@NoArgsConstructor
@Entity(name = "klkl_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	@Column(
		name = "profile_image_url",
		length = 500,
		nullable = false
	)
	private String profileImageUrl;

	@Column(
		name = "name",
		unique = true,
		length = 30,
		nullable = false
	)
	private String name;

	@Column(
		name = "gender",
		length = 1,
		nullable = false
	)
	private Gender gender;

	@Column(
		name = "age",
		nullable = false
	)
	private Integer age;

	@Column(
		name = "description",
		length = 100
	)
	private String description;

	// TODO: created_at 이름으로 json나가야 함
	@Column(
		name = "created_at",
		nullable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

	private User(
		final String name,
		final Gender gender,
		final Integer age,
		final String description
	) {
		this.profileImageUrl = UserConstants.DEFAULT_PROFILE_IMAGE_URL;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.description = description;
		this.createdAt = LocalDateTime.now();
	}

	public static User of(
		final String name,
		final Gender gender,
		final Integer age,
		final String description
	) {
		return new User(name, gender, age, description);
	}

	public void update(
		final String name,
		final Gender gender,
		final Integer age,
		final String description
	) {
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.description = description;
	}

	public void updateProfileImageUrl(final String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}
