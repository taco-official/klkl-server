package taco.klkl.domain.comment.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.comment.dto.request.CommentRequestDto;
import taco.klkl.domain.user.domain.User;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// @ManyToOne
	// @JoinColumn(name = "product", nullable = false)
	// private Product product;

	@Column(
		name = "product_id",
		nullable = false
	)
	private Long productId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(
		name = "content",
		nullable = false,
		length = 400)
	private String content;

	@Column(
		name = "created_at",
		nullable = false
	)
	private LocalDate date;

	private Comment(Long productId, User user, String content) {
		this.productId = productId;
		this.user = user;
		this.content = content;
		this.date = LocalDate.now();
	}

	public void update(CommentRequestDto commentUpdateRequestDto) {
		this.content = commentUpdateRequestDto.content();
	}

	public static Comment of(Long productId, User user, String content) {
		return new Comment(productId, user, content);
	}
}
