package taco.klkl.global.config.security;

import java.util.Arrays;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityEndpoint {
	PUBLIC(new RequestMatcher[]{
		new AntPathRequestMatcher("/"),
		new AntPathRequestMatcher("/static/**"),
		new AntPathRequestMatcher("/login/**"),
		new AntPathRequestMatcher("/error"),
		new AntPathRequestMatcher("/favicon.ico"),

		// swagger
		new AntPathRequestMatcher("/swagger-ui/**"),
		new AntPathRequestMatcher("/swagger-ui.html"),
		new AntPathRequestMatcher("/api-docs/**"),
		new AntPathRequestMatcher("/v3/api-docs/**"),

		// h2 console
		new AntPathRequestMatcher("/h2-console/**"),

		// klkl api
		new AntPathRequestMatcher("/v1/login/**"),
		new AntPathRequestMatcher("/v1/oauth2/**"),
		new AntPathRequestMatcher("/v1/members/**"),
		new AntPathRequestMatcher("/v1/regions/**"),
		new AntPathRequestMatcher("/v1/countries/**"),
		new AntPathRequestMatcher("/v1/cities/**"),
		new AntPathRequestMatcher("/v1/currencies/**"),
		new AntPathRequestMatcher("/v1/categories/**"),
		new AntPathRequestMatcher("/v1/subcategories/**"),
		new AntPathRequestMatcher("/v1/search/**"),
	}),
	USER_ROLE(new RequestMatcher[]{
		new AntPathRequestMatcher("/v1/me/**"),
		new AntPathRequestMatcher("/v1/likes/**"),
		new AntPathRequestMatcher("/v1/notifications/**"),
		new AntPathRequestMatcher("/v1/logout/**"),
	}),
	BOTH(new RequestMatcher[]{
		new AntPathRequestMatcher("/v1/products/**"),
	}),
	;

	private final RequestMatcher[] matchers;

	public static boolean isBothEndpoint(HttpServletRequest request) {
		return Arrays.stream(BOTH.getMatchers())
			.anyMatch(matcher -> matcher.matches(request));
	}

	public static boolean isPublicEndpoint(HttpServletRequest request) {
		return Arrays.stream(PUBLIC.getMatchers())
			.anyMatch(matcher -> matcher.matches(request));
	}
}
