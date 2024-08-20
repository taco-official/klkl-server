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

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@GetMapping()
	public ResponseEntity<Void> oauthKakao() {
		String location =
			"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri="
				+ redirectUri;

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(location)).build();
	}

	@GetMapping("/code")
	public UserDetailResponse processKakaoOauth2(@RequestParam("code") String code) throws
		JsonProcessingException {

		return oauth2KakaoService.processOauth2(code);
	}

}
