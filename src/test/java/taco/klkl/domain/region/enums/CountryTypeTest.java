package taco.klkl.domain.region.enums;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import taco.klkl.domain.region.domain.CountryType;

class CountryTypeTest {

	@Test
	@DisplayName("부분 문자열을 포함하는 CountryType 찾기 - 정상 케이스")
	void testGetCountryTypesByPartialString() {
		// given
		String partialString = "일본";

		// when
		List<CountryType> result = CountryType.getCountryTypesByPartialString(partialString);

		// then
		assertThat(result).containsExactly(CountryType.JAPAN);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CountryType 찾기 - 여러 결과")
	void testGetCountryTypesByPartialStringMultipleResults() {
		// given
		String partialString = "국";

		// when
		List<CountryType> result = CountryType.getCountryTypesByPartialString(partialString);

		// then
		assertThat(result).containsExactlyInAnyOrder(CountryType.CHINA, CountryType.THAILAND, CountryType.USA);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CountryType 찾기 - 결과 없음")
	void testGetCountryTypesByPartialStringNoResults() {
		// given
		String partialString = "없음";

		// when
		List<CountryType> result = CountryType.getCountryTypesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CountryType 찾기 - 빈 문자열")
	void testGetCountryTypesByPartialStringEmpty() {
		// given
		String partialString = "";

		// when
		List<CountryType> result = CountryType.getCountryTypesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CountryType 찾기 - null 입력")
	void testGetCountryTypesByPartialStringNull() {
		// given
		String partialString = null;

		// when
		List<CountryType> result = CountryType.getCountryTypesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

}
