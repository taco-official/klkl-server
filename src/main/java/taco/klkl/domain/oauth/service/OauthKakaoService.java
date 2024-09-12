package taco.klkl.domain.oauth.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import taco.klkl.domain.member.dto.response.MemberDetailResponse;

@Service
public interface OauthKakaoService {
	MemberDetailResponse kakaoOauthLogin(final String code) throws JsonProcessingException;
}
