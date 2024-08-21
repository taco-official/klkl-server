package taco.klkl.domain.oauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.oauth.dao.OauthRepository;
import taco.klkl.domain.oauth.domain.Oauth;
import taco.klkl.domain.oauth.dto.request.KakaoUserInfoRequest;
import taco.klkl.domain.user.domain.Gender;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.request.UserCreateRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.domain.user.service.UserService;
import taco.klkl.global.util.UserUtil;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class Oauth2KakaoServiceImpl implements Oauth2KakaoService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final OauthRepository oauthRepository;
	private final UserService userService;
	private final UserUtil userUtil;

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

	// TODO: requestAccessToken, requestAccessToken 외부 API요청은 트랜잭션밖에서 해야함
	@Override
	public UserDetailResponse processOauth2(String code) throws JsonProcessingException {

		String accessToken = requestAccessToken(code);
		KakaoUserInfoRequest userInfoRequest = requestUserInfo(accessToken);

		return loginUser(userInfoRequest);
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
	 * AccessToken으로 사용자 정보요청을 수행합니다.
	 * @param accessToken
	 * @return
	 * @throws JsonProcessingException
	 */
	private KakaoUserInfoRequest requestUserInfo(String accessToken) throws JsonProcessingException {
		HttpHeaders headers = getRequestUserInfoHeader(accessToken);

		HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
		return requestPostUserInfo(httpEntity);
	}

	/**
	 * 사용자정보 요청을 위한 헤더를 구성합니다.
	 * @param accessToken
	 * @return
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

		return KakaoUserInfoRequest.of(id, trimDoubleQuote(nickname), trimDoubleQuote(profile));
	}

	/**
	 * "를 제거합니다.
	 * @param string
	 * @return
	 */
	private String trimDoubleQuote(String string) {
		return string.replaceAll("^\"|\"$", "");
	}

	/**
	 * Oauth의 결과로 사용자 로그인 처리를 수행합니다.
	 * TODO: 현재 더미유저 데이터로 인해 최초요청은 에러발생
	 * @param userInfoRequest
	 * @return
	 */
	private UserDetailResponse loginUser(KakaoUserInfoRequest userInfoRequest) {

		Long oauthId = userInfoRequest.id();

		// 이미 oauth로그인 기록이 있는 유저를 처리합니다.
		if (oauthRepository.existsByOauthId(oauthId)) {
			Oauth oauth = oauthRepository.findFirstByOauthId(oauthId);
			User user = oauth.getUser();
			return UserDetailResponse.from(user);
		}

		// TODO: 닉네임 생성해서 줘야함
		UserCreateRequest userCreateRequest = UserCreateRequest.of(
			userInfoRequest.nickname() + userInfoRequest.id(),
			Gender.MALE.getDescription(),
			0,
			userInfoRequest.profileImage(),
			""
		);

		// 유저를 DB에 생성합니다.
		UserDetailResponse userDetailResponse = userService.registerUser(userCreateRequest);

		// oauth정보를 DB에 생성합니다.
		User user = userUtil.findUserById(userDetailResponse.id());
		Oauth oauth = Oauth.of(user, userInfoRequest.id());
		oauthRepository.save(oauth);

		return userDetailResponse;
	}
}
