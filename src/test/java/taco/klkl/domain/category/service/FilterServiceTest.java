package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.category.dao.FilterRepository;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.FilterName;
import taco.klkl.domain.category.exception.FilterNotFoundException;

@DisplayName("FilterService 테스트")
class FilterServiceTest {

	@InjectMocks
	private FilterService filterService;

	@Mock
	private FilterRepository filterRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("존재하는 ID로 필터 조회 시 필터를 반환해야 한다")
	void testGetFilterEntityById_ExistingId() {
		// Given
		long filterId = 1L;
		Filter expectedFilter = Filter.of(FilterName.CILANTRO);
		when(filterRepository.findById(filterId)).thenReturn(Optional.of(expectedFilter));

		// When
		Filter result = filterService.getFilterEntityById(filterId);

		// Then
		assertNotNull(result);
		assertEquals(expectedFilter, result);
		verify(filterRepository).findById(filterId);
	}

	@Test
	@DisplayName("존재하지 않는 ID로 필터 조회 시 FilterNotFoundException을 던져야 한다")
	void testGetFilterEntityById_NonExistingId() {
		// Given
		long nonExistingId = 999L;
		when(filterRepository.findById(nonExistingId)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(FilterNotFoundException.class, () -> {
			filterService.getFilterEntityById(nonExistingId);
		});
		verify(filterRepository).findById(nonExistingId);
	}
}
