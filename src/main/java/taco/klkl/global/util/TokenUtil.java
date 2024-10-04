package taco.klkl.global.util;

import static taco.klkl.global.common.constants.TokenConstants.ACCESS_TOKEN;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenUtil {

	public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
		Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(3600);
		cookie.setAttribute("SameSite", "Strict");
		response.addCookie(cookie);
	}
}
