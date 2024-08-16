package taco.klkl.domain.region.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityTypeTest {

	@Test
	@DisplayName("부분 문자열을 포함하는 CityType 찾기 - 정상 케이스")
	void testGetCityTypesByPartialString() {
		// given
		String partialString = "도쿄";

		// when
		List<CityType> result = CityType.getCityTypesByPartialString(partialString);

		// then
		assertThat(result).containsExactly(CityType.TOKYO);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CityType 찾기 - 여러 결과")
	void testGetCityTypesByPartialStringMultipleResults() {
		// given
		String partialString = "오";

		// when
		List<CityType> result = CityType.getCityTypesByPartialString(partialString);

		// then
		assertThat(result).containsExactlyInAnyOrder(CityType.OSAKA, CityType.FUKUOKA, CityType.OKINAWA);
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CityType 찾기 - 결과 없음")
	void testGetCityTypesByPartialStringNoResults() {
		// given
		String partialString = "없음";

		// when
		List<CityType> result = CityType.getCityTypesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CityType 찾기 - 빈 문자열")
	void testGetCityTypesByPartialStringEmpty() {
		// given
		String partialString = "";

		// when
		List<CityType> result = CityType.getCityTypesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("부분 문자열을 포함하는 CityType 찾기 - null 입력")
	void testGetCityTypesByPartialStringNull() {
		// given
		String partialString = null;

		// when
		List<CityType> result = CityType.getCityTypesByPartialString(partialString);

		// then
		assertThat(result).isEmpty();
	}
}
