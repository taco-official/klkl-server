package taco.klkl.domain.oauth.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.oauth.dto.request.KakaoUserInfoRequest;
import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Service
public interface Oauth2KakaoLoginService {
	UserDetailResponse loginUser(KakaoUserInfoRequest userInfoRequest);
}
