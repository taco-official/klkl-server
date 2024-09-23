package taco.klkl.domain.token.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taco.klkl.domain.token.domain.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByName(final String name);

	Optional<Token> findByAccessToken(final String accessToken);
}
