package taco.klkl.domain.oauth.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.oauth.service.Oauth2KakaoService;
import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Slf4j
@RestController
@RequestMapping("/v1/oauth/kakao")
@RequiredArgsConstructor
public class Oauth2KakaoController {

	private final Oauth2KakaoService oauth2KakaoService;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
	private String authorizationUri;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${api.main-url}")
	private String mainUrl;

	/**
	 * 클라이언트를 Kakao Oauth URL로 리다이렉트합니다.
	 * @return
	 */
	@GetMapping()
	public ResponseEntity<Void> oauthKakao() {
		String location = getKakaoOauthLocation();

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(location)).build();
	}

	/**
	 * Kakao 리다이렉트 받은 code값으로 사용자 로그인처리를 합니다.
	 * @param code
	 * @return
	 * @throws JsonProcessingException
	 */
	// TODO: JWT적용시 토큰 관리 로직 추가
	@GetMapping("/code")
	public UserDetailResponse processKakaoOauth2(@RequestParam("code") String code) throws
		JsonProcessingException {

		return oauth2KakaoService.kakaoOauthLogin(code);
	}

	/**
	 * Kakao Oauth 요청 URL을 구성합니다.
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
