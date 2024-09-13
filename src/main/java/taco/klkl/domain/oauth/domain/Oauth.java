package taco.klkl.domain.oauth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Enumerated(EnumType.STRING)
	@Column(
		name = "provider",
		nullable = false
	)
	private Provider provider;

	@Column(
		name = "provider_id",
		nullable = false
	)
	private Long providerId;

	private Oauth(
		final Member member,
		final Provider provider,
		final Long providerId
	) {
		this.member = member;
		this.provider = provider;
		this.providerId = providerId;
	}

	public static Oauth of(
		final Member member,
		final Provider provider,
		final Long providerId
	) {
		return new Oauth(member, provider, providerId);
	}
}
