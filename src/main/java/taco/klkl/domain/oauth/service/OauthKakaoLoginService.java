package taco.klkl.domain.oauth.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.oauth.dto.request.KakaoMemberInfoRequest;

@Service
public interface OauthKakaoLoginService {
	MemberDetailResponse loginMember(final KakaoMemberInfoRequest userInfoRequest);
}
