package taco.klkl.domain.category.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubcategoryNameTest {

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 정상 케이스")
	void testFindByPartialString() {
		// given
		String partialString = "라면";

		// when
		List<SubcategoryName> result = SubcategoryName.findByPartialString(partialString);

		// then
		assertThat(result).containsExactly(SubcategoryName.INSTANT_FOOD);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 여러 결과")
	void testFindByPartialStringMultipleResults() {
		// given
		String partialString = "잡화";

		// when
		List<SubcategoryName> result = SubcategoryName.findByPartialString(partialString);

		// then
		assertThat(result).containsExactlyInAnyOrder(SubcategoryName.KITCHEN_SUPPLIES,
			SubcategoryName.BATHROOM_SUPPLIES);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 결과 없음")
	void testFindByPartialStringNoResults() {
		// given
		String partialString = "없음";

		// when
		List<SubcategoryName> result = SubcategoryName.findByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - 빈 문자열")
	void testFindByPartialStringEmpty() {
		// given
		String partialString = "";

		// when
		List<SubcategoryName> result = SubcategoryName.findByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 SubcategoryName 찾기 - null 입력")
	void testFindByPartialStringNull() {
		// given
		String partialString = null;

		// when
		List<SubcategoryName> result = SubcategoryName.findByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

}
