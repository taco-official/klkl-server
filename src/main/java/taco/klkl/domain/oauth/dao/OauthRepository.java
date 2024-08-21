package taco.klkl.domain.oauth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.oauth.domain.Oauth;

public interface OauthRepository extends JpaRepository<Oauth, Long> {
	boolean existsByOauthId(Long oauthId);

	Oauth findFirstByOauthId(Long oauthId);
}
