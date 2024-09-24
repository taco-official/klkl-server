package taco.klkl.domain.member.dto.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.domain.Member;

class MemberSimpleResponseTest {
	@Test
	@DisplayName("회원 간단 정보를 반환하는 DTO 테스트")
	public void testMemberSimpleResponseDto() {
		// given
		Long id = 1L;
		ImageResponse image = new ImageResponse(2L, "url");
		String name = "이름";

		// when
		MemberSimpleResponse memberResponse = new MemberSimpleResponse(id, image, name);

		// then
		assertThat(memberResponse.id()).isEqualTo(id);
		assertThat(memberResponse.image().id()).isEqualTo(image.id());
		assertThat(memberResponse.name()).isEqualTo(name);
	}

	@Test
	@DisplayName("Member 객체로부터 MemberSimpleResponse 생성 테스트")
	public void testFrom() {
		// given
		String name = "이름";

		// when
		Member member = Member.of(name);
		MemberSimpleResponse memberResponse = MemberSimpleResponse.from(member);

		// then
		assertThat(memberResponse.name()).isEqualTo(name);
	}
}
