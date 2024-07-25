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
import taco.klkl.global.common.enums.Gender;

@Getter
@NoArgsConstructor
@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	@Column(length = 500, nullable = false)
	private String profile = "image/default.jpg";

	@Column(unique = true, length = 30, nullable = false)
	private String name;

	@Column(length = 1, nullable = false)
	private Gender gender;

	@Column(nullable = false)
	private Integer age;

	@Column(length = 100)
	private String description;

	// TODO: created_at 이름으로 json나가야 함
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void prePersist() {
		if (this.profile == null) {
			this.profile = "image/default.jpg";
		}
		this.createdAt = LocalDateTime.now();
	}

	private User(String profile, String name, Gender gender, Integer age, String description) {
		this.profile = profile;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.description = description;
	}

	public static User of(String profile, String name, Gender gender, Integer age, String description) {
		return new User(profile, name, gender, age, description);
	}
}
