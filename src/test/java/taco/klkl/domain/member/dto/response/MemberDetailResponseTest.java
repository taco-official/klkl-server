package taco.klkl.domain.member.dto.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.domain.Member;

public class MemberDetailResponseTest {
	@Test
	@DisplayName("회원 상세 정보를 반환하는 DTO 테스트")
	public void testMemberDetailResponseDto() {
		// given
		Long id = 1L;
		ImageResponse image = new ImageResponse(2L, "url");
		String name = "이름";
		String description = "자기소개";

		// when
		MemberDetailResponse memberResponse = new MemberDetailResponse(id, image, name, description);

		// then
		assertThat(memberResponse.id()).isEqualTo(id);
		assertThat(memberResponse.image().id()).isEqualTo(image.id());
		assertThat(memberResponse.name()).isEqualTo(name);
		assertThat(memberResponse.description()).isEqualTo(description);
	}

	@Test
	@DisplayName("Member 객체로부터 MemberDetailResponse 생성 테스트")
	public void testFrom() {
		// given
		String name = "이름";

		// when
		Member member = Member.ofUser(name, null, null);
		MemberDetailResponse memberResponse = MemberDetailResponse.from(member);

		// then
		assertThat(memberResponse.name()).isEqualTo(name);
	}
}
