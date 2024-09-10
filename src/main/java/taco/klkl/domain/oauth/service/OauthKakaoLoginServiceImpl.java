package taco.klkl.domain.oauth.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.oauth.dao.OauthRepository;
import taco.klkl.domain.oauth.domain.Oauth;
import taco.klkl.domain.oauth.dto.request.KakaoUserInfoRequest;
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
public class OauthKakaoLoginServiceImpl implements OauthKakaoLoginService {

	private final OauthRepository oauthRepository;
	private final UserService userService;
	private final UserUtil userUtil;

	/**
	 * Oauth의 결과로 사용자 로그인 처리를 수행합니다.
	 * TODO: 현재 더미유저 데이터로 인해 최초요청은 에러발생
	 * @param userInfoRequest
	 * @return
	 */
	public UserDetailResponse loginUser(final KakaoUserInfoRequest userInfoRequest) {

		final Long oauthMemberId = userInfoRequest.oauthMemberId();

		// 이미 oauth로그인 기록이 있는 유저를 처리합니다.
		if (oauthRepository.existsByOauthMemberId(oauthMemberId)) {
			final Oauth oauth = oauthRepository.findFirstByOauthMemberId(oauthMemberId);
			final User user = oauth.getUser();
			return UserDetailResponse.from(user);
		}

		final User user = registerUser(userInfoRequest);
		final Oauth oauth = Oauth.of(user, userInfoRequest.oauthMemberId());
		oauthRepository.save(oauth);

		return UserDetailResponse.from(user);
	}

	private User registerUser(final KakaoUserInfoRequest userInfoRequest) {

		final String name = userUtil.createUsername(userInfoRequest.nickname(), userInfoRequest.oauthMemberId());

		// TODO: 성별, 나이는 기본값으로 넣고 있습니다.
		final UserCreateRequest userCreateRequest = UserCreateRequest.of(
			name,
			""
		);

		// 유저를 DB에 생성합니다.
		return userService.createUser(userCreateRequest);
	}
}
