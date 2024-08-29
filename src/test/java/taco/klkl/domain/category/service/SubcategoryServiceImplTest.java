package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.category.dao.subcategory.SubcategoryRepository;
import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.exception.subcategory.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.subcategory.SubcategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SubcategoryServiceImplTest {

	@InjectMocks
	private SubcategoryServiceImpl subcategoryService;

	@Mock
	private SubcategoryRepository subcategoryRepository;

	private final Category category = Category.of(CategoryType.FOOD);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryType.SNACK);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryType.BEVERAGE);
	private final Subcategory subcategory3 = Subcategory.of(category, SubcategoryType.DRINKS);

	@Test
	@DisplayName("Query id가 Long type으로 입력되고 subcategory가 존재하는 경우")
	public void testFindSubcategoriesBySubcategoryIdsWithExistIds() {
		//given
		final List<Long> subcategoriesIds = Arrays.asList(1L, 2L, 3L);
		final List<Subcategory> subcategoriesList = Arrays.asList(subcategory1, subcategory2, subcategory3);

		when(subcategoryRepository.findAllById(subcategoriesIds)).thenReturn(subcategoriesList);

		//when
		List<Subcategory> result = subcategoryService.findSubcategoriesBySubcategoryIds(subcategoriesIds);

		//then
		assertNotNull(result);
		assertEquals(3, result.size());

		assertEquals(SubcategoryType.SNACK.getName(), result.get(0).getName());
		assertEquals(SubcategoryType.BEVERAGE.getName(), result.get(1).getName());
		assertEquals(SubcategoryType.DRINKS.getName(), result.get(2).getName());

		verify(subcategoryRepository, times(1)).findAllById(subcategoriesIds);
	}

	@Test
	@DisplayName("Query id가 Long type으로 입력되고 subcategory가 존재하지 않는 경우")
	public void testFindSubcategoriesBySubcategoryIdsWithNotExistIds() {
		//given
		final List<Long> subcategoriesIds = Arrays.asList(1L, 2L, 3L);

		//when
		when(subcategoryRepository.findAllById(subcategoriesIds)).thenThrow(SubcategoryNotFoundException.class);

		//then
		assertThrows(SubcategoryNotFoundException.class, () -> {
			subcategoryService.findSubcategoriesBySubcategoryIds(subcategoriesIds);
		});
		verify(subcategoryRepository, times(1)).findAllById(subcategoriesIds);
	}

	@Test
	@DisplayName("SubcategoryName리스트로 Subcategory 조회")
	void testFindAllSubcategoriesByPartialString() {
		// given
		String partialName = "foo";
		List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2, subcategory3);
		SubcategoryResponse subcategory1ResponseDto = SubcategoryResponse.from(subcategory1);
		SubcategoryResponse subcategory2ResponseDto = SubcategoryResponse.from(subcategory2);
		SubcategoryResponse subcategory3ResponseDto = SubcategoryResponse.from(subcategory3);

		when(subcategoryRepository.findAllByNameLike(partialName)).thenReturn(subcategories);

		// when
		List<SubcategoryResponse> subcategoryResponseList = subcategoryService
			.findAllSubcategoriesByPartialString(partialName);

		// then
		Assertions.assertThat(subcategoryResponseList.size()).isEqualTo(3);
		Assertions.assertThat(subcategoryResponseList)
			.containsExactly(subcategory1ResponseDto, subcategory2ResponseDto, subcategory3ResponseDto);
	}
}
