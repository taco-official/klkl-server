package taco.klkl.domain.member.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.service.MemberService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ResponseUtil;
import taco.klkl.global.util.TokenUtil;

@WebMvcTest(MemberController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class MemberControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@MockBean
	private TokenUtil tokenUtil;

	@MockBean
	MemberService memberService;

	@MockBean
	MemberUtil memberUtil;

	private Member member;
	private MemberDetailResponse memberDetailResponse;

	@BeforeEach
	public void setUp() {
		member = Member.ofUser("name", null, null);
		memberDetailResponse = MemberDetailResponse.from(member);
	}

	@Test
	@DisplayName("내 정보 조회 API 테스트")
	public void testGetMe() throws Exception {
		// given
		when(memberUtil.getCurrentMember()).thenReturn(member);
		when(memberService.getMemberById(any())).thenReturn(memberDetailResponse);

		// when & then
		mockMvc.perform(get("/v1/me")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(nullValue())))
			.andExpect(jsonPath("$.data.name", is(memberDetailResponse.name())))
			.andExpect(jsonPath("$.data.description", is(memberDetailResponse.description())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
