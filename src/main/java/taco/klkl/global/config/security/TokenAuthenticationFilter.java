package taco.klkl.global.config.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
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


@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private static final String TOKEN_PREFIX = "Bearer ";

	private final TokenProvider tokenProvider;
	private final ResponseUtil responseUtil;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			String accessToken = resolveToken(request);
			if (tokenProvider.validateToken(accessToken)) {
				setAuthentication(accessToken);
			} else {
				String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
				if (StringUtils.hasText(reissueAccessToken)) {
					setAuthentication(reissueAccessToken);
					response.setHeader(AUTHORIZATION, TOKEN_PREFIX + reissueAccessToken);
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
		String token = request.getHeader(AUTHORIZATION);
		if (ObjectUtils.isEmpty(token) || !token.startsWith(TOKEN_PREFIX)) {
			return null;
		}
		return token.substring(TOKEN_PREFIX.length());
	}
}
