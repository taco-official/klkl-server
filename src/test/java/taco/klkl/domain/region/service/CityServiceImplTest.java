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

import taco.klkl.domain.region.dao.CityRepository;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.enums.CityType;

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
	@DisplayName("id로 도시 존재 여부 확인 테스트")
	void testGetCityEntityById_True() {
		// given
		City mockCity = mock(City.class);
		when(cityRepository.findById(1L))
			.thenReturn(Optional.ofNullable(mockCity));

		// when
		City city = cityService.getCityEntityById(1L);

		// then
		assertThat(city).isEqualTo(mockCity);
	}

	@Test
	@DisplayName("CityType리스트로 도시 조회")
	void testGetCitiesByCityTypes() {
		// given
		List<CityType> cityTypes = Arrays.asList(CityType.BEIJING, CityType.BORACAY);
		List<City> cities = Arrays.asList(city1, city2);
		CityResponseDto city1ResponseDto = CityResponseDto.from(city1);
		CityResponseDto city2ResponseDto = CityResponseDto.from(city2);
		List<CityResponseDto> cityResponseDtos = Arrays.asList(city1ResponseDto, city2ResponseDto);
		when(cityRepository.findAllByNameIn(cityTypes)).thenReturn(cities);

		// when
		List<CityResponseDto> cityResponseDtoList = cityService.getAllCitiesByCityTypes(cityTypes);

		// then
		assertThat(cityResponseDtoList).hasSize(cityTypes.size());
		assertThat(cityResponseDtoList).containsExactly(city1ResponseDto, city2ResponseDto);
	}
}
