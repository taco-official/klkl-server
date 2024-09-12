package taco.klkl.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import taco.klkl.domain.member.dao.MemberRepository;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.request.MemberCreateRequest;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.global.util.MemberUtil;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImplTest.class);

	@Mock
	MemberRepository memberRepository;

	@Mock
	MemberUtil memberUtil;

	@InjectMocks
	MemberServiceImpl memberService;

	@Test
	@DisplayName("내 정보 조회 서비스 테스트")
	public void testGetMemberById() {
		// given

		Member member = mock(Member.class);
		when(member.getId()).thenReturn(1L);
		when(member.getName()).thenReturn("testUser");
		when(member.getDescription()).thenReturn("테스트입니다.");
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

		// when
		MemberDetailResponse userDto = memberService.getMemberById(1L);

		// then
		assertThat(userDto.id()).isEqualTo(member.getId());
		assertThat(userDto.name()).isEqualTo(member.getName());
		assertThat(userDto.description()).isEqualTo(member.getDescription());
	}

	@Test
	@DisplayName("사용자 등록 서비스 테스트")
	public void testCreateUser() {
		// given
		MemberCreateRequest requestDto = new MemberCreateRequest(
			"이상화",
			"저는 이상화입니다."
		);
		Member member = Member.of(
			requestDto.name(),
			requestDto.description()
		);
		when(memberRepository.save(any(Member.class))).thenReturn(member);

		// when
		Member member1 = memberService.createUser(requestDto);
		MemberDetailResponse responseDto = MemberDetailResponse.from(member1);

		// then
		assertThat(responseDto.name()).isEqualTo(requestDto.name());
		assertThat(responseDto.description()).isEqualTo(requestDto.description());
	}
}
