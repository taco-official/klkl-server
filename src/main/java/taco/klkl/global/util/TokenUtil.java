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
		cookie.setSecure(false); // TODO: HTTPS 사용 시 활성화
		cookie.setPath("/");
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
	}
}
