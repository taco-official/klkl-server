package taco.klkl.domain.user.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.image.domain.Image;

@Getter
@NoArgsConstructor
@Entity(name = "klkl_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "image_id")
	private Image image;

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

	@Column(
		name = "created_at",
		nullable = false,
		updatable = false
	)
	private LocalDateTime createdAt;

	private User(
		final String name,
		final String description
	) {
		this.name = name;
		this.description = description;
		this.createdAt = LocalDateTime.now();
	}

	public static User of(
		final String name,
		final String description
	) {
		return new User(name, description);
	}

	public void update(
		final String name,
		final String description
	) {
		this.name = name;
		this.description = description;
	}

	public void updateImage(final Image image) {
		this.image = image;
	}
}
