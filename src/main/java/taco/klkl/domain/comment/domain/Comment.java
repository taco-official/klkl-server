package taco.klkl.domain.comment.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.comment.dto.request.CommentCreateUpdateRequestDto;
import taco.klkl.domain.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(
		name = "product_id",
		nullable = false
	)
	private Long productId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(
		name = "content",
		nullable = false,
		length = 400
	)
	private String content;

	@Column(
		name = "created_at",
		nullable = false
	)
	private LocalDateTime createdAt;

	private Comment(
		final Long productId,
		final User user,
		final String content
	) {
		this.productId = productId;
		this.user = user;
		this.content = content;
		this.createdAt = LocalDateTime.now();
	}

	public void update(final CommentCreateUpdateRequestDto commentUpdateRequestDto) {
		this.content = commentUpdateRequestDto.content();
	}

	public static Comment of(
		final Long productId,
		final User user,
		final String content
	) {
		return new Comment(productId, user, content);
	}
}
