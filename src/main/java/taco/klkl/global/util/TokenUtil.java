package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public final class TokenUtil {

	public static void addTokenCookie(
		HttpServletResponse response,
		String name,
		String value,
		int maxAge
	) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // TODO: HTTPS 사용 시 활성화
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	private TokenUtil() {
	}
}
