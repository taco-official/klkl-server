package taco.klkl.domain.oauth.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;
import taco.klkl.domain.token.service.TokenProvider;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private static final String URI = "/auth/success";

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException, ServletException {

		final String accessToken = tokenProvider.generateAccessToken(authentication);
		tokenProvider.generateRefreshToken(authentication, accessToken);

		String redirectUrl = UriComponentsBuilder.fromUriString(URI)
				.queryParam("accessToken", accessToken)
				.build().toUriString();

		response.sendRedirect(redirectUrl);
	}
}
