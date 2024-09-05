package taco.klkl.domain.region.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.region.exception.country.CountryNotFoundException;
import taco.klkl.domain.region.service.country.CountryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

	private static final Logger log = LoggerFactory.getLogger(CountryServiceImplTest.class);
	@InjectMocks
	CountryServiceImpl countryService;

	@Mock
	CountryRepository countryRepository;

	private final Region region = Region.from(RegionType.NORTHEAST_ASIA);
	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN);
	private final Country country1 = Country.of(
		CountryType.JAPAN,
		region,
		"photo",
		currency1);
	private final Country country2 = Country.of(
		CountryType.TAIWAN,
		region,
		"photo",
		currency1);
	private final City city1 = City.of(CityType.OSAKA, country1);
	private final City city2 = City.of(CityType.KYOTO, country1);
	private final List<City> cities = Arrays.asList(city1, city2);

	@Test
	@DisplayName("모든 국가 조회 테스트")
	void testFindAllCountries() {
		// given
		List<Country> countries = Arrays.asList(country1, country2);
		when(countryRepository.findAll()).thenReturn(countries);

		// when
		List<CountryResponse> findCountries = countryService.findAllCountries();

		// then
		assertThat(findCountries.size()).isEqualTo(countries.size());
		assertThat(findCountries.get(0).name()).isEqualTo(countries.get(0).getName());
		assertThat(findCountries.get(1).name()).isEqualTo(countries.get(1).getName());
	}

	@Test
	@DisplayName("id로 국가조회 테스트")
	void testFindCountryById() {
		// given
		when(countryRepository.findById(400L)).thenReturn(Optional.of(country1));

		// when
		CountryResponse findCountry = countryService.findCountryById(400L);

		// then
		assertThat(findCountry).isEqualTo(CountryResponse.from(country1));
	}

	@Test
	@DisplayName("국가 조회 실패 테스트")
	void testFindCountryByIdFail() {
		// given
		when(countryRepository.findById(400L)).thenThrow(CountryNotFoundException.class);

		// when & then
		Assertions.assertThrows(CountryNotFoundException.class, () ->
			countryService.findCountryById(400L));

		verify(countryRepository, times(1)).findById(400L);
	}

	@Test
	@DisplayName("국가와 도시 조회")
	void testGetCountryWithCitiesById() {
		// given
		Country mockCountry = mock(Country.class);
		when(countryRepository.findById(400L)).thenReturn(Optional.of(mockCountry));
		when(mockCountry.getCities()).thenReturn(cities);

		// when
		List<CityResponse> findCountries = countryService.findCitiesByCountryId(400L);

		// then
		assertThat(findCountries.size()).isEqualTo(cities.size());
		assertThat(findCountries.get(0).name()).isEqualTo(cities.get(0).getName());
		assertThat(findCountries.get(1).name()).isEqualTo(cities.get(1).getName());
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
		List<CountrySimpleResponse> countrySimpleResponses = countryService.findAllCountriesByPartialString(
			partialName);

		// then
		assertThat(countrySimpleResponses).hasSize(2);
		assertThat(countrySimpleResponses).containsExactlyInAnyOrder(country1ResponseDto, country2ResponseDto);
	}
}
