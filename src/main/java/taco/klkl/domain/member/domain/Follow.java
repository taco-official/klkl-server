package taco.klkl.domain.member.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"follower_id", "following_id"})
	}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "follow_id")
	private Long id;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "follower_id",
		nullable = false
	)
	private Member follower;

	@ManyToOne(
		fetch = FetchType.LAZY,
		optional = false
	)
	@JoinColumn(
		name = "following_id",
		nullable = false
	)
	private Member following;

	@Column(
		name = "created_at",
		nullable = false
	)
	private LocalDateTime createdAt;

	private Follow(final Member follower, final Member following) {
		this.follower = follower;
		this.following = following;
		this.createdAt = LocalDateTime.now();
	}

	public static Follow of(final Member follower, final Member following) {
		return new Follow(follower, following);
	}

}
