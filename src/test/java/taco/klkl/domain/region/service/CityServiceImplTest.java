package taco.klkl.domain.region.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.region.dao.CityRepository;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

	@InjectMocks
	CityServiceImpl cityService;

	@Mock
	CityRepository cityRepository;

	@Test
	@DisplayName("id로 도시 존재 여부 확인 테스트")
	void isExitsCity_True() {
		// given
		when(cityRepository.existsById(1L)).thenReturn(true);

		// when
		boolean result = cityService.existsCityById(1L);

		// then
		assertThat(result).isTrue();
	}
}
