package taco.klkl.domain.region.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.region.dao.CityRepository;
import taco.klkl.domain.region.domain.City;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

	@InjectMocks
	CityServiceImpl cityService;

	@Mock
	CityRepository cityRepository;

	@Test
	@DisplayName("id로 도시 존재 여부 확인 테스트")
	void testGetCityById_True() {
		// given
		City mockCity = mock(City.class);
		when(cityRepository.findById(1L))
			.thenReturn(Optional.ofNullable(mockCity));

		// when
		City city = cityService.getCityById(1L);

		// then
		assertThat(city).isEqualTo(mockCity);
	}
}
