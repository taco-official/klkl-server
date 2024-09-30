package taco.klkl.domain.token.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.token.domain.Token;

@Service
public interface TokenService {

	void saveOrUpdate(final String name, final String refreshToken, final String accessToken);

	Token findByAccessTokenOrThrow(final String accessToken);

	void updateToken(final String accessToken, final Token token);
}
