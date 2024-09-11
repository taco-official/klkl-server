package taco.klkl.domain.region.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.region.dto.response.country.CountryWithCitiesResponse;
import taco.klkl.domain.region.service.country.CountryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

	@InjectMocks
	CountryServiceImpl countryService;

	@Mock
	CountryRepository countryRepository;

	private final Region region = Region.from(RegionType.NORTHEAST_ASIA);
	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN);
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region,
		"wallpaper",
		currency1);
	private final Country country2 = Country.of(
		CountryType.TAIWAN,
		region,
		"wallpaper",
		currency1);

	@Test
	@DisplayName("id로 국가조회 테스트")
	void testFindCountryById() {
		// given
		when(countryRepository.findById(400L)).thenReturn(Optional.of(country1));

		// when
		CountryResponse findCountry = countryService.getCountryById(400L);

		// then
		assertThat(findCountry).isEqualTo(CountryResponse.from(country1));
	}

	@Test
	@DisplayName("부분 문자열로 국가 조회")
	void testGetCountriesByCountryTypes() {
		// given
		String partialName = "foo";
		CountrySimpleResponse country1ResponseDto = CountrySimpleResponse.from(country1);
		CountrySimpleResponse country2ResponseDto = CountrySimpleResponse.from(country2);
		when(countryRepository.findAllByNameContaining(partialName)).thenReturn(Arrays.asList(country1, country2));

		// when
		List<CountrySimpleResponse> countryWithCitiesResponse = countryService.findAllCountriesByPartialString(
			partialName);

		// then
		assertThat(countryWithCitiesResponse).hasSize(2);
		assertThat(countryWithCitiesResponse).containsExactlyInAnyOrder(country1ResponseDto, country2ResponseDto);
	}
}
