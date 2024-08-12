package taco.klkl.domain.notification.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.global.common.constants.NotificationConstants;

@Getter
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "is_read")
	@ColumnDefault(NotificationConstants.DEFAULT_IS_READ_STRING)
	private Boolean isRead;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@OneToOne
	@JoinColumn(name = "comment_id")
	private Comment comment;

	@PrePersist
	protected void prePersist() {
		if (isRead == null) {
			isRead = NotificationConstants.DEFAULT_IS_READ_VALUE;
		}
	}

	protected Notification(Comment comment) {
		this.comment = comment;
		this.createdAt = LocalDateTime.now();
	}

	public static Notification of(Comment comment) {
		return new Notification(comment);
	}

	public void read() {
		isRead = NotificationConstants.UPDATED_IS_READ_VALUE;
	}
}

// 어떤 사람이 댓글을 담 -> 댓글이 달림과 동시에 댓글 알림도 save되어야함.
// -> 상품을 작성한 유저의 아이디 값도 저장되어 있어야지 댓글 알림에서 유저 아이디 조회 시 불필요한 조인을 줄일 수 있음
// -> 이렇게되면 새로고침하거나 웹사이트에 처음 들어왔을 경우에만 알림이 노출. -> OKYY 그 사이트는 이 방식이라 함.
// -> 그리고 누가 댓글을 달았는지 보여줄 것인가? -> 보여줄 경우
// 이 사람이 남긴 게시물 조회 거기서
