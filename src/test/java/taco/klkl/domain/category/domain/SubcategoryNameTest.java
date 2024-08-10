package taco.klkl.domain.category.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubcategoryNameTest {

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 정상 케이스")
	void testGetSubcategoryNamesByPartialString() {
		// given
		String partialString = "라면";

		// when
		List<SubcategoryName> result = SubcategoryName.getSubcategoryNamesByPartialString(partialString);

		// then
		assertThat(result).containsExactly(SubcategoryName.INSTANT_FOOD);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 여러 결과")
	void testGetSubcategoryNamesByPartialStringMultipleResults() {
		// given
		String partialString = "잡화";

		// when
		List<SubcategoryName> result = SubcategoryName.getSubcategoryNamesByPartialString(partialString);

		// then
		assertThat(result).containsExactlyInAnyOrder(SubcategoryName.KITCHEN_SUPPLIES,
			SubcategoryName.BATHROOM_SUPPLIES);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 결과 없음")
	void testGetSubcategoryNamesByPartialStringNoResults() {
		// given
		String partialString = "없음";

		// when
		List<SubcategoryName> result = SubcategoryName.getSubcategoryNamesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 빈 문자열")
	void testGetSubcategoryNamesByPartialStringEmpty() {
		// given
		String partialString = "";

		// when
		List<SubcategoryName> result = SubcategoryName.getSubcategoryNamesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - null 입력")
	void testGetSubcategoryNamesByPartialStringNull() {
		// given
		String partialString = null;

		// when
		List<SubcategoryName> result = SubcategoryName.getSubcategoryNamesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

}
