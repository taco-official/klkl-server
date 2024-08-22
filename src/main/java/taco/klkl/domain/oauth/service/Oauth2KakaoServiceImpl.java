package taco.klkl.domain.oauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.oauth.dto.request.KakaoUserInfoRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.util.StringUtil;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class Oauth2KakaoServiceImpl implements Oauth2KakaoService {

	private final Oauth2KakaoLoginServiceImpl oauth2KakaoLoginService;

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String tokenUri;

	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String userInfoUri;

	@Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
	private String grantType;

	@Value("${api.main-url}")
	private String mainUrl;

	/**
	 * AccessToken으로 사용자 정보요청을 수행합니다.
	 * @param code
	 * @return
	 * @throws JsonProcessingException
	 */
	@Override
	public UserDetailResponse kakaoOauthLogin(String code) throws JsonProcessingException {
		String accessToken = requestAccessToken(code);
		HttpHeaders headers = getRequestUserInfoHeader(accessToken);

		HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
		return oauth2KakaoLoginService.loginUser(requestPostUserInfo(httpEntity));
	}

	/**
	 * 카카오 AccessToken 발급 api 요청을 처리합니다.
	 * @param code
	 * @return
	 * @throws JsonProcessingException
	 */
	private String requestAccessToken(String code) throws JsonProcessingException {
		HttpHeaders headers = getRequestTokenHeader();
		String requestBody = getRequestTokenBody(code);

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = requestPostAccessToken(httpEntity);

		// 응답을 json형식의 객체로 구성합니다.
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getBody());

		return jsonNode.get("access_token").toString();
	}

	/**
	 * AccessToken 요청을 위한 헤더를 구성합니다.
	 * @return
	 */
	private HttpHeaders getRequestTokenHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		return headers;
	}

	/**
	 * AccessToken 요청을 위한 Body를 구성합니다.
	 * @param code
	 * @return
	 */
	private String getRequestTokenBody(String code) {
		return "grant_type=" + grantType
			+ "&client_id=" + clientId
			+ "&code=" + code
			+ "&redirect_uri=" + mainUrl + redirectUri;
	}

	/**
	 * Kakao API로 AccessToken 발급 POST 요청을 수행합니다.
	 * @param httpEntity
	 * @return
	 */
	private ResponseEntity<String> requestPostAccessToken(HttpEntity<String> httpEntity) {
		return restTemplate.exchange(tokenUri, HttpMethod.POST, httpEntity,
			String.class);
	}

	/**
	 * 사용자정보 요청을 위한 헤더를 구성합니다.
	 * @param accessToken
	 * @returnx
	 */
	private HttpHeaders getRequestUserInfoHeader(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		return headers;
	}

	/**
	 * Kakao API로 사용자정보를 POST요청합니다.
	 * @param httpEntity
	 * @return
	 * @throws JsonProcessingException
	 */
	private KakaoUserInfoRequest requestPostUserInfo(HttpEntity<Object> httpEntity) throws JsonProcessingException {
		ResponseEntity<String> exchange = restTemplate.exchange(userInfoUri, HttpMethod.POST, httpEntity, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(exchange.getBody());

		Long id = Long.parseLong((jsonNode.get("id").toString()));
		String nickname = (jsonNode.get("properties").get("nickname").toString());
		String profile = (jsonNode.get("properties").get("profile_image").toString());

		return KakaoUserInfoRequest.of(id, StringUtil.trimDoubleQuote(nickname), StringUtil.trimDoubleQuote(profile));
	}

}
