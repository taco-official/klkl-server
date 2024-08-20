package taco.klkl.domain.oauth.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import taco.klkl.domain.user.dto.response.UserDetailResponse;

@Service
public interface Oauth2KakaoService {
	UserDetailResponse processOauth2(String code) throws JsonProcessingException;
}
