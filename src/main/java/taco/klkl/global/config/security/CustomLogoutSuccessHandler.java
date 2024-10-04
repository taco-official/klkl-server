package taco.klkl.global.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import taco.klkl.global.util.TokenUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	private final TokenUtil tokenUtil;

	@Override
	public void onLogoutSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		tokenUtil.clearAccessTokenCookie(response);
		response.setStatus(HttpStatus.NO_CONTENT.value());
		response.getWriter().flush();
	}
}