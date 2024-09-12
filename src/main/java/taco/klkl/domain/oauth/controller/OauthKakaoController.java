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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.oauth.service.OauthKakaoService;

@Slf4j
@RestController
@RequestMapping("/v1/oauth/kakao")
@RequiredArgsConstructor
@Tag(name = "9. 인증/인가", description = "인증/인가 API")
public class OauthKakaoController {

	private final OauthKakaoService oauthKakaoService;

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
	 * Kakao 리다이렉트 받은 code값으로 사용자 로그인처리를 합니다.
	 * @param code
	 * @return
	 * @throws JsonProcessingException
	 */
	// TODO: JWT적용시 토큰 관리 로직 추가
	@GetMapping("/code")
	@Operation(summary = "kakao 사용자 정보 가져오기", description = "카카오 API를 사용하여 사용자 정보를 가져옵니다.")
	public MemberDetailResponse processKakaoOauth2(@RequestParam("code") final String code) throws
		JsonProcessingException {

		return oauthKakaoService.kakaoOauthLogin(code);
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
