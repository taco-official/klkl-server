package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.FilterName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryFilter;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.FilterWithSubcategoryResponse;

@ExtendWith(MockitoExtension.class)
public class SubcategoryFilterServiceTest {
	@InjectMocks
	private SubcategoryFilterService subcategoryFilterService;

	@Mock
	private Subcategory subcategory1;

	@Mock
	private Filter filter1;

	@Mock
	private Filter filter2;

	@Mock
	private SubcategoryFilter subcategoryFilter1;

	@Mock
	private SubcategoryFilter subcategoryFilter2;

	@BeforeEach
	public void setUp() {
		List<SubcategoryFilter> subcategoryFilters = Arrays.asList(subcategoryFilter1, subcategoryFilter2);

		when(subcategory1.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);
		when(subcategory1.getSubcategoryFilters()).thenReturn(subcategoryFilters);
		when(subcategoryFilter1.getFilter()).thenReturn(filter1);
		when(subcategoryFilter2.getFilter()).thenReturn(filter2);
		when(filter1.getName()).thenReturn(FilterName.CONVENIENCE_STORE);
		when(filter2.getName()).thenReturn(FilterName.CILANTRO);
	}

	@Test
	@DisplayName("해당 서비스가 SubcategoryWithFilterResponseDto를 반환하는지 테스트")
	public void testGetFilters() {
		// given
		final List<Subcategory> subcategoryList = Arrays.asList(subcategory1);

		// when
		final List<FilterWithSubcategoryResponse> result = subcategoryFilterService.getFilters(subcategoryList);

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(SubcategoryName.INSTANT_FOOD.getKoreanName(), result.get(0).subcategory());
		assertEquals(2, result.get(0).filters().size());
		assertEquals(FilterName.CONVENIENCE_STORE.getKoreanName(), result.get(0).filters().get(0).name());
		assertEquals(FilterName.CILANTRO.getKoreanName(), result.get(0).filters().get(1).name());
	}
}
