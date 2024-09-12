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
import taco.klkl.domain.member.domain.Member;

@Getter
@Entity(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

	@Id
	@Column(name = "oauth_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	private Member member;

	@Column(
		name = "oauth_member_id",
		nullable = false
	)
	private Long oauthMemberId;

	private Oauth(final Member member, final Long oauthMemberId) {
		this.member = member;
		this.oauthMemberId = oauthMemberId;
	}

	public static Oauth of(final Member member, final Long oauth2MemberId) {
		return new Oauth(member, oauth2MemberId);
	}
}
