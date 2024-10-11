package taco.klkl.global.config.security;

import java.io.IOException;

import org.springframework.http.HttpMethod;
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
		return HttpMethod.GET.matches(request.getMethod())
			&& SecurityEndpoint.isPublicEndpoint(request);
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		final String accessToken = tokenUtil.resolveToken(request);

		try {
			if (tokenProvider.validateToken(accessToken)) {
				setAuthentication(accessToken);
			} else {
				final String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
				setAuthentication(reissueAccessToken);
				tokenUtil.addAccessTokenCookie(response, reissueAccessToken);
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
		if (StringUtils.hasText(accessToken)) {
			throw new TokenInvalidException();
		}
		Authentication authentication = tokenProvider.getAuthentication(accessToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void handleTokenException(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain,
		CustomException ex
	) throws IOException, ServletException {
		if (HttpMethod.GET.matches(request.getMethod()) && SecurityEndpoint.isBothEndpoint(request)) {
			proceedWithoutAuthentication(request, response, filterChain);
			return;
		}
		SecurityContextHolder.clearContext();
		responseUtil.sendErrorResponse(response, ex);
	}

	private void proceedWithoutAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}
}
