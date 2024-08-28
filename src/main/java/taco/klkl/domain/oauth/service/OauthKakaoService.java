package taco.klkl.domain.oauth.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Service
public interface OauthKakaoService {
	UserDetailResponse kakaoOauthLogin(final String code) throws JsonProcessingException;
}
