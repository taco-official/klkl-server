package taco.klkl.global.config.security;

import java.io.IOException;
import java.util.Arrays;

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

	private final TokenProvider tokenProvider;
	private final ResponseUtil responseUtil;
	private final TokenUtil tokenUtil;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return Arrays.stream(SecurityEndpoint.PUBLIC.getMatchers())
			.anyMatch(matcher -> matcher.matches(request));
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			String accessToken = tokenUtil.resolveToken(request);
			if (tokenProvider.validateToken(accessToken)) {
				setAuthentication(accessToken);
			} else {
				String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
				if (StringUtils.hasText(reissueAccessToken)) {
					setAuthentication(reissueAccessToken);
					tokenUtil.addAccessTokenCookie(response, reissueAccessToken);
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

	private void setAuthentication(final String accessToken) {
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
