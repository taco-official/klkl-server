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

import taco.klkl.domain.region.dao.CountryRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.CityType;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.CountryType;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.CurrencyType;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.domain.RegionType;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.dto.response.CountrySimpleResponse;
import taco.klkl.domain.region.exception.CountryNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

	private static final Logger log = LoggerFactory.getLogger(CountryServiceImplTest.class);
	@InjectMocks
	CountryServiceImpl countryService;

	@Mock
	CountryRepository countryRepository;

	private final Region region = Region.of(RegionType.NORTHEAST_ASIA);
	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN, "flagUrl");
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
	private final City city1 = City.of(country1, CityType.OSAKA);
	private final City city2 = City.of(country1, CityType.KYOTO);
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
		assertThat(findCountries.get(0).name()).isEqualTo(countries.get(0).getName().getKoreanName());
		assertThat(findCountries.get(1).name()).isEqualTo(countries.get(1).getName().getKoreanName());
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
		assertThat(findCountries.get(0).name()).isEqualTo(cities.get(0).getName().getKoreanName());
		assertThat(findCountries.get(1).name()).isEqualTo(cities.get(1).getName().getKoreanName());
	}

	@Test
	@DisplayName("CountryType리스트로 국가 조회")
	void testGetCountriesByCountryTypes() {
		// given
		List<CountryType> countryTypes = Arrays.asList(CountryType.JAPAN, CountryType.TAIWAN);
		CountrySimpleResponse country1ResponseDto = CountrySimpleResponse.from(country1);
		CountrySimpleResponse country2ResponseDto = CountrySimpleResponse.from(country2);
		when(countryRepository.findAllByNameIn(countryTypes)).thenReturn(Arrays.asList(country1, country2));

		// when
		List<CountrySimpleResponse> countrySimpleResponses = countryService.getAllCountriesByCountryTypes(
			countryTypes);

		// then
		assertThat(countrySimpleResponses).hasSize(countryTypes.size());
		assertThat(countrySimpleResponses).containsExactlyInAnyOrder(country1ResponseDto, country2ResponseDto);
	}
}
