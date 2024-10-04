package taco.klkl.domain.token.service;

import taco.klkl.domain.token.domain.Token;

public interface TokenService {

	void saveOrUpdate(final String name, final String refreshToken, final String accessToken);

	Token findByAccessTokenOrThrow(final String accessToken);

	void updateToken(final String accessToken, final Token token);

	void deleteToken(final String name);
}
