package taco.klkl.domain.member.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static taco.klkl.global.common.constants.TestConstants.TEST_UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.member.dao.MemberRepository;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.member.service.MemberService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.util.MemberUtil;
import taco.klkl.global.util.ResponseUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig.class)
@WithMockUser(username = TEST_UUID, roles = "USER")
public class MemberIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	private MemberUtil memberUtil;

	@Test
	public void testMembersMe() throws Exception {
		// given, when
		Member me = memberUtil.getCurrentMember();
		MemberDetailResponse memberResponse = memberService.getMemberById(me.getId());

		// then
		mockMvc.perform(get("/v1/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.id", is(memberResponse.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(memberResponse.name())))
			.andExpect(jsonPath("$.data.description", is(memberResponse.description())))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
		;
	}
}
