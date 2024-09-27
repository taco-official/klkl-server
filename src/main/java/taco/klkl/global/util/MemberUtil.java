package taco.klkl.global.util;

import java.util.Random;

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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return memberRepository.findByName(authentication.getName())
				.orElseThrow(MemberNotFoundException::new);
	}

	public static String generateRandomTag() {
		return String.format("%04d", new Random().nextInt(10000));
	}
}
