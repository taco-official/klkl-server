package taco.klkl.global.config.security;

import java.io.IOException;

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
import taco.klkl.global.error.exception.CustomException;
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
		return "GET".equalsIgnoreCase(request.getMethod())
			&& SecurityEndpoint.isPublicEndpoint(request);
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		final String accessToken = tokenUtil.resolveToken(request);
		final boolean isBothEndpoint = SecurityEndpoint.isBothEndpoint(request);
		final boolean isGetRequest = "GET".equalsIgnoreCase(request.getMethod());

		if (!StringUtils.hasText(accessToken)) {
			if (isGetRequest && isBothEndpoint) {
				proceedWithoutAuthentication(request, response, filterChain);
				return;
			}
		}

		try {
			if (tokenProvider.validateToken(accessToken)) {
				setAuthentication(accessToken);
			} else {
				final String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
				if (StringUtils.hasText(reissueAccessToken)) {
					setAuthentication(reissueAccessToken);
					tokenUtil.addAccessTokenCookie(response, reissueAccessToken);
				}
			}
		} catch (TokenInvalidException | TokenExpiredException e) {
			handleTokenException(request, response, filterChain, e);
			return;
		} catch (Exception e) {
			handleTokenException(request, response, filterChain, new UnauthorizedException());
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void setAuthentication(final String accessToken) {
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void handleTokenException(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain,
		CustomException ex
	) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		if ("GET".equalsIgnoreCase(request.getMethod()) && SecurityEndpoint.isBothEndpoint(request)) {
			proceedWithoutAuthentication(request, response, filterChain);
		} else {
			responseUtil.sendErrorResponse(response, ex);
		}
	}

	private void proceedWithoutAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}
}
