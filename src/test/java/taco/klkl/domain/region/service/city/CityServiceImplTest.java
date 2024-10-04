package taco.klkl.domain.region.service.city;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.region.dao.city.CityRepository;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

	@InjectMocks
	CityServiceImpl cityService;

	@Mock
	CityRepository cityRepository;

	@Mock
	Country country;

	private final City city1 = City.of(CityType.BEIJING, country);
	private final City city2 = City.of(CityType.BORACAY, country);

	@Test
	@DisplayName("부분 문자열로 도시 조회")
	void testGetCitiesByCityTypes() {
		// given
		String partialName = "foo";
		List<City> cities = Arrays.asList(city1, city2);
		CitySimpleResponse city1ResponseDto = CitySimpleResponse.from(city1);
		CitySimpleResponse city2ResponseDto = CitySimpleResponse.from(city2);
		when(cityRepository.findAllByNameContaining(partialName)).thenReturn(cities);

		// when
		List<CitySimpleResponse> citySimpleResponseList = cityService.findAllCitiesByPartialString(partialName);

		// then
		assertThat(citySimpleResponseList).hasSize(2);
		assertThat(citySimpleResponseList).containsExactly(city1ResponseDto, city2ResponseDto);
	}
}
