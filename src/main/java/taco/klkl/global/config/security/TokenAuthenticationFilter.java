package taco.klkl.global.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.auth.exception.UnauthorizedException;
import taco.klkl.domain.token.exception.TokenExpiredException;
import taco.klkl.domain.token.exception.TokenInvalidException;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.util.ResponseUtil;
import taco.klkl.global.util.TokenUtil;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";

	private final TokenProvider tokenProvider;
	private final ResponseUtil responseUtil;

	@Value("${jwt.expiration.access}")
	private int accessTokenExpiration;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			String accessToken = resolveToken(request);
			if (StringUtils.hasText(accessToken)) {
				if (tokenProvider.validateToken(accessToken)) {
					setAuthentication(accessToken);
				} else {
					String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
					if (StringUtils.hasText(reissueAccessToken)) {
						setAuthentication(reissueAccessToken);
						TokenUtil.addTokenCookie(
							response,
							ACCESS_TOKEN_COOKIE_NAME,
							reissueAccessToken,
							accessTokenExpiration
						);
					}
				}
			}
		} catch (TokenInvalidException | TokenExpiredException e) {
			SecurityContextHolder.clearContext();
			responseUtil.sendErrorResponse(response, e);
			return;
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			responseUtil.sendErrorResponse(response, new UnauthorizedException());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void setAuthentication(String accessToken) {
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String resolveToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
				.findFirst()
				.map(Cookie::getValue))
			.orElse(null);
	}
}
