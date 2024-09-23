package taco.klkl.global.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import taco.klkl.domain.token.exception.TokenInvalidException;

import java.io.IOException;

public class TokenExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (TokenInvalidException e) {
			response.sendError(e.getErrorCode().getHttpStatus().value(), e.getMessage());
		}
	}
}
