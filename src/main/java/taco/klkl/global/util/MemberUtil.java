package taco.klkl.global.util;

import java.time.Instant;
import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.member.dao.MemberRepository;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.exception.MemberNotFoundException;
import taco.klkl.global.common.constants.MemberConstants;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberUtil {

	private final MemberRepository memberRepository;

	/**
	 * TODO: 인증정보를 확인해 유저 엔티티를 리턴한다.
	 * 현재 유저 조회
	 * @return
	 */
	public Member getCurrentMember() {
		return getTestMember();
	}

	public String createUsername(final String name, final Long oauthMemberId) {

		String createdName = generateUsername(name, oauthMemberId);

		while (memberRepository.existsByName(createdName)) {
			createdName = generateUsername(name, oauthMemberId);
		}

		return createdName;
	}

	private Member getTestMember() {
		return memberRepository.findById(1L)
			.orElseThrow(MemberNotFoundException::new);
	}

	private String generateUsername(final String name, final Long oauthMemberId) {

		final Long currentTimeMillis = Instant.now().toEpochMilli();
		final int hashCode = Objects.hash(name, oauthMemberId, currentTimeMillis);

		final String suffix = String.format("%04d", Math.abs(hashCode) % MemberConstants.USERNAME_SUFFIX_MOD);

		return name + suffix;
	}

}
