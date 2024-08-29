package taco.klkl.domain.region.service;

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

import taco.klkl.domain.region.dao.CityRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.CityType;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.dto.response.CityResponse;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

	@InjectMocks
	CityServiceImpl cityService;

	@Mock
	CityRepository cityRepository;

	@Mock
	Country country;

	private final City city1 = City.of(country, CityType.BEIJING);
	private final City city2 = City.of(country, CityType.BORACAY);

	@Test
	@DisplayName("CityType리스트로 도시 조회")
	void testGetCitiesByCityTypes() {
		// given
		List<CityType> cityTypes = Arrays.asList(CityType.BEIJING, CityType.BORACAY);
		List<City> cities = Arrays.asList(city1, city2);
		CityResponse city1ResponseDto = CityResponse.from(city1);
		CityResponse city2ResponseDto = CityResponse.from(city2);
		List<CityResponse> cityResponses = Arrays.asList(city1ResponseDto, city2ResponseDto);
		when(cityRepository.findAllByNameIn(cityTypes)).thenReturn(cities);

		// when
		List<CityResponse> cityResponseList = cityService.findAllCitiesByCityTypes(cityTypes);

		// then
		assertThat(cityResponseList).hasSize(cityTypes.size());
		assertThat(cityResponseList).containsExactly(city1ResponseDto, city2ResponseDto);
	}
}
