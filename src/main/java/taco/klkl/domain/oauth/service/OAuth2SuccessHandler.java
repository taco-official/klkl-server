package taco.klkl.domain.oauth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.util.TokenUtil;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;

	@Value("${jwt.redirect}")
	private String redirectUri;

	@Value("${jwt.expiration.access}")
	private int accessTokenExpiration;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		final String accessToken = tokenProvider.generateAccessToken(authentication);
		tokenProvider.generateRefreshToken(authentication, accessToken);
		TokenUtil.addTokenCookie(response, "access_token", accessToken, accessTokenExpiration);
		response.sendRedirect(redirectUri);
	}
}
