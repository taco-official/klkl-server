package taco.klkl.domain.token.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.token.dao.TokenRepository;
import taco.klkl.domain.token.domain.Token;
import taco.klkl.domain.token.exception.TokenInvalidException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

	private final TokenRepository tokenRepository;

	@Override
	@Transactional
	public void saveOrUpdate(final String name, final String refreshToken, final String accessToken) {
		tokenRepository.findByName(name)
			.ifPresentOrElse(
				existingToken -> existingToken.update(refreshToken, accessToken), () -> {
					Token newToken = Token.of(name, refreshToken, accessToken);
					tokenRepository.save(newToken);
				}
			);
	}

	@Override
	public Token findByAccessTokenOrThrow(final String accessToken) {
		return tokenRepository.findByAccessToken(accessToken)
			.orElseThrow(TokenInvalidException::new);
	}

	@Override
	@Transactional
	public void updateToken(final String accessToken, final Token token) {
		token.update(token.getRefreshToken(), accessToken);
	}

	@Override
	@Transactional
	public void deleteToken(final String name) {
		tokenRepository.deleteByName(name);
	}
}
