package taco.klkl.domain.oauth2.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.oauth2.dao.Oauth2Repository;
import taco.klkl.domain.oauth2.domain.Oauth2;
import taco.klkl.domain.oauth2.dto.request.KakaoUserInfoRequest;
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
public class Oauth2KakaoLoginServiceImpl implements Oauth2KakaoLoginService {

	private final Oauth2Repository oauth2Repository;
	private final UserService userService;
	private final UserUtil userUtil;

	/**
	 * Oauth의 결과로 사용자 로그인 처리를 수행합니다.
	 * TODO: 현재 더미유저 데이터로 인해 최초요청은 에러발생
	 * @param userInfoRequest
	 * @return
	 */
	public UserDetailResponse loginUser(KakaoUserInfoRequest userInfoRequest) {

		Long oauthId = userInfoRequest.id();

		// 이미 oauth로그인 기록이 있는 유저를 처리합니다.
		if (oauth2Repository.existsByOauthId(oauthId)) {
			Oauth2 oauth2 = oauth2Repository.findFirstByOauthId(oauthId);
			User user = oauth2.getUser();
			return UserDetailResponse.from(user);
		}

		User user = registerUser(userInfoRequest);
		Oauth2 oauth2 = Oauth2.of(user, userInfoRequest.id());
		oauth2Repository.save(oauth2);

		return UserDetailResponse.from(user);
	}

	private User registerUser(KakaoUserInfoRequest userInfoRequest) {

		String name = userUtil.createUserName(userInfoRequest.nickname(), userInfoRequest.id());

		// TODO: 성별, 나이는 기본값으로 넣고 있습니다.
		UserCreateRequest userCreateRequest = UserCreateRequest.of(
			name,
			Gender.MALE.getDescription(),
			0,
			userInfoRequest.profileImage(),
			""
		);

		// 유저를 DB에 생성합니다.
		UserDetailResponse userDetailResponse = userService.registerUser(userCreateRequest);

		return userUtil.findUserByName(userDetailResponse.name());
	}
}
