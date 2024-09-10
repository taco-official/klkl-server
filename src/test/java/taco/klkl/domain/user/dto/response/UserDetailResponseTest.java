package taco.klkl.domain.user.dto.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.UserConstants;

public class UserDetailResponseTest {
	@Test
	@DisplayName("유저 상세 정보를 반환하는 DTO 테스트")
	public void testUserDetailResponseDto() {
		// given
		Long id = 1L;
		String profile = "image/profileUrl.png";
		String name = "이름";
		String description = "자기소개";
		int totalLikeCount = 100;

		// when
		UserDetailResponse userDetail = new UserDetailResponse(id, profile, name, description, totalLikeCount);

		// then
		assertThat(userDetail.id()).isEqualTo(id);
		assertThat(userDetail.profileUrl()).isEqualTo(profile);
		assertThat(userDetail.name()).isEqualTo(name);
		assertThat(userDetail.description()).isEqualTo(description);
		assertThat(userDetail.totalLikeCount()).isEqualTo(totalLikeCount);
	}

	@Test
	@DisplayName("User 객체로부터 UserDetailResponse 생성 테스트")
	public void testFrom() {
		// given
		String name = "이름";
		String description = "자기소개";

		// when
		User user = User.of(name, description);
		UserDetailResponse userDetail = UserDetailResponse.from(user);

		// then
		assertThat(userDetail.name()).isEqualTo(name);
		assertThat(userDetail.description()).isEqualTo(description);
		assertThat(userDetail.totalLikeCount()).isEqualTo(UserConstants.DEFAULT_TOTAL_LIKE_COUNT);
	}
}
