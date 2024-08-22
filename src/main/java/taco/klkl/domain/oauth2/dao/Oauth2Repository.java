package taco.klkl.domain.oauth2.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.oauth2.domain.Oauth2;

public interface Oauth2Repository extends JpaRepository<Oauth2, Long> {
	boolean existsByOauth2MemberId(final Long oauthId);

	Oauth2 findFirstByOauth2MemberId(final Long oauthId);
}
