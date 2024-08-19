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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.global.common.constants.NotificationConstants;

@Getter
@Entity(name = "notification")
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

	private Notification(final Comment comment) {
		this.isRead = NotificationConstants.DEFAULT_IS_READ_VALUE;
		this.comment = comment;
		this.createdAt = LocalDateTime.now();
	}

	public static Notification of(final Comment comment) {
		return new Notification(comment);
	}

	public void read() {
		isRead = NotificationConstants.UPDATED_IS_READ_VALUE;
	}
}

