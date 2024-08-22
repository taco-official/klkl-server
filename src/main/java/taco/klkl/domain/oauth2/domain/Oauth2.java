package taco.klkl.domain.oauth2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taco.klkl.domain.user.domain.User;

@Getter
@Entity(name = "oauth2")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth2 {

	@Id
	@Column(name = "oauth2_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@Column(
		name = "oauth2_member_id",
		nullable = false
	)
	private Long oauth2MemberId;

	private Oauth2(final User user, final Long oauth2MemberId) {
		this.user = user;
		this.oauth2MemberId = oauth2MemberId;
	}

	public static Oauth2 of(final User user, final Long oauth2MemberId) {
		return new Oauth2(user, oauth2MemberId);
	}
}
