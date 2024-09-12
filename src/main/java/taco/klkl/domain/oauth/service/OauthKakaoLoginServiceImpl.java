package taco.klkl.domain.oauth.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.request.MemberCreateRequest;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.service.MemberService;
import taco.klkl.domain.oauth.dao.OauthRepository;
import taco.klkl.domain.oauth.domain.Oauth;
import taco.klkl.domain.oauth.dto.request.KakaoMemberInfoRequest;
import taco.klkl.global.util.MemberUtil;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class OauthKakaoLoginServiceImpl implements OauthKakaoLoginService {

	private final OauthRepository oauthRepository;
	private final MemberService memberService;
	private final MemberUtil memberUtil;

	/**
	 * Oauth의 결과로 사용자 로그인 처리를 수행합니다.
	 * TODO: 현재 더미유저 데이터로 인해 최초요청은 에러발생
	 * @param userInfoRequest
	 * @return
	 */
	public MemberDetailResponse loginUser(final KakaoMemberInfoRequest userInfoRequest) {

		final Long oauthMemberId = userInfoRequest.oauthMemberId();

		// 이미 oauth로그인 기록이 있는 유저를 처리합니다.
		if (oauthRepository.existsByOauthMemberId(oauthMemberId)) {
			final Oauth oauth = oauthRepository.findFirstByOauthMemberId(oauthMemberId);
			final Member member = oauth.getMember();
			return MemberDetailResponse.from(member);
		}

		final Member member = registerUser(userInfoRequest);
		final Oauth oauth = Oauth.of(member, userInfoRequest.oauthMemberId());
		oauthRepository.save(oauth);

		return MemberDetailResponse.from(member);
	}

	private Member registerUser(final KakaoMemberInfoRequest userInfoRequest) {

		final String name = memberUtil.createUsername(userInfoRequest.nickname(), userInfoRequest.oauthMemberId());

		// TODO: 성별, 나이는 기본값으로 넣고 있습니다.
		final MemberCreateRequest memberCreateRequest = MemberCreateRequest.of(
			name,
			""
		);

		// 유저를 DB에 생성합니다.
		return memberService.createUser(memberCreateRequest);
	}
}
