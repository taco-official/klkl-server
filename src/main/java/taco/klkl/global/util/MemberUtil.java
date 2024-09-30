package taco.klkl.global.util;

import java.util.Random;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.member.dao.MemberRepository;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.exception.MemberNotFoundException;

@Component
@RequiredArgsConstructor
public class MemberUtil {

	private final MemberRepository memberRepository;

	/**
	 * TODO: 인증정보를 확인해 유저 엔티티를 리턴한다.
	 * 현재 유저 조회
	 *
	 * @return
	 */
	public Member getCurrentMember() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UUID uuid = getCurrentMemberUuid(authentication.getName());
		return memberRepository.findByUuid(uuid)
				.orElseThrow(MemberNotFoundException::new);
	}

	private UUID getCurrentMemberUuid(final String authName) {
		try {
			return UUID.fromString(authName);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException();
		}
	}

	public static String generateRandomTag() {
		return String.format("%04d", new Random().nextInt(10000));
	}
}
