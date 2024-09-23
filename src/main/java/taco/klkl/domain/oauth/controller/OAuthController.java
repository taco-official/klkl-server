package taco.klkl.domain.oauth.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/oauth/kakao")
@RequiredArgsConstructor
@Tag(name = "9. 인증/인가", description = "인증/인가 API")
public class OAuthController {

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
	private String authorizationUri;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${api.main-url}")
	private String mainUrl;

	/**
	 * 클라이언트를 Kakao OAuth URL로 리다이렉트합니다.
	 * @return
	 */
	@GetMapping("/login")
	@Operation(summary = "kakao 간편로그인 요청", description = "카카오 oauth를 사용하여 로그인 처리합니다.")
	public ResponseEntity<Void> oauthKakao() {
		final String location = getKakaoOauthLocation();
		final URI locationUri = URI.create(location);

		return ResponseEntity
			.status(HttpStatus.FOUND)
			.location(locationUri)
			.build();
	}

	/**
	 * Kakao OAuth 요청 URL을 구성합니다.
	 * @return
	 */
	private String getKakaoOauthLocation() {
		StringBuilder location = new StringBuilder();
		location.append(authorizationUri)
			.append("?response_type=").append("code")
			.append("&client_id=").append(clientId)
			.append("&redirect_uri=").append(mainUrl).append(redirectUri);

		return location.toString();
	}

}
