package taco.klkl.domain.oauth.domain;

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
@Entity(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

	@Id
	@Column(name = "oauth_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private User user;

	@Column(
		name = "oauth_member_id",
		nullable = false
	)
	private Long oauthMemberId;

	private Oauth(final User user, final Long oauthMemberId) {
		this.user = user;
		this.oauthMemberId = oauthMemberId;
	}

	public static Oauth of(final User user, final Long oauth2MemberId) {
		return new Oauth(user, oauth2MemberId);
	}
}
