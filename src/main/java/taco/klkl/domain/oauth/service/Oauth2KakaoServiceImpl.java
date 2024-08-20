package taco.klkl.domain.oauth.service;

import org.springframework.beans.factory.annotation.Value;
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
import taco.klkl.domain.user.dao.UserRepository;
import taco.klkl.domain.user.domain.Gender;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2KakaoServiceImpl implements Oauth2KakaoService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final UserRepository userRepository;

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;

	@Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
	private String tokenUri;

	@Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
	private String userInfoUri;

	@Override
	public UserDetailResponse processOauth2(String code) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String grantType = "authorization_code";

		StringBuilder body = new StringBuilder();
		body.append("grant_type=").append(grantType)
			.append("&client_id=").append(clientId)
			.append("&code=").append(code)
			.append("&redirect_uri=").append(redirectUri);

		String requestBody = body.toString();

		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(tokenUri, HttpMethod.POST, httpEntity,
			String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getBody());

		String accessToken = jsonNode.get("access_token").toString();
		headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		httpEntity = new HttpEntity<>(headers);
		response = restTemplate.exchange(userInfoUri, HttpMethod.POST, httpEntity, String.class);
		jsonNode = objectMapper.readTree(response.getBody());

		String userId = jsonNode.get("id").toString();
		String nickName = jsonNode.get("properties").get("nickname").toString();
		String profile = jsonNode.get("properties").get("profile_image").toString();
		profile = profile.replaceAll("^\"|\"$", "");
		userId = userId.replaceAll("^\"|\"$", "");
		nickName = nickName.replaceAll("^\"|\"$", "");

		User user = User.of(profile, userId, Gender.MALE, 28, nickName);
		userRepository.save(user);

		return UserDetailResponse.from(user);
	}
}