package taco.klkl.domain.user.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@PrePersist
	protected void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	private User(
		final String profileImageUrl,
		final String name,
		final Gender gender,
		final Integer age,
		final String description
	) {
		this.profileImageUrl = profileImageUrl;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.description = description;
	}

	public static User of(
		final String profileImageUrl,
		final String name,
		final Gender gender,
		final Integer age,
		final String description
	) {
		return new User(profileImageUrl, name, gender, age, description);
	}

	public void update(
		final String profileImageUrl,
		final String name,
		final Gender gender,
		final Integer age,
		final String description
	) {
		this.profileImageUrl = profileImageUrl;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.description = description;
	}
}
