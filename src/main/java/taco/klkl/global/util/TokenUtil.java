package taco.klkl.global.util;

import static taco.klkl.global.common.constants.TokenConstants.ACCESS_TOKEN;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

@Component
public class TokenUtil {

	public String resolveToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
			.flatMap(cookies -> Arrays.stream(cookies)
				.filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
				.findFirst()
				.map(Cookie::getValue))
			.orElse(null);
	}

	public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
		Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
	}

	public void clearAccessTokenCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(ACCESS_TOKEN, null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
