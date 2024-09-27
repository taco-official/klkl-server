package taco.klkl.domain.auth.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.auth.domain.PrincipalDetails;
import taco.klkl.domain.auth.dto.response.OAuth2UserInfo;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.service.MemberService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final MemberService memberService;

	@Override
	@Transactional
	public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		final Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

		final String registrationId = userRequest.getClientRegistration().getRegistrationId();
		final String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		final OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(
			registrationId,
			oAuth2UserAttributes
		);

		final Member member = memberService.createOrGetMemberByOAuth2(oAuth2UserInfo);
		return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
	}
}
