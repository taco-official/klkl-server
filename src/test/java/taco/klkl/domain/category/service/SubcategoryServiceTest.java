package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.category.dao.SubcategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SubcategoryServiceTest {
	@Mock
	private SubcategoryRepository subcategoryRepository;

	@InjectMocks
	private SubcategoryService subcategoryService;

	private final Category category = Category.of(CategoryName.FOOD);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryName.SNACK);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryName.BEVERAGE);
	private final Subcategory subcategory3 = Subcategory.of(category, SubcategoryName.DRINKS);

	@Test
	@DisplayName("Query id가 Long type으로 입력되고 subcategory가 존재하는 경우")
	public void testGetSubcategoryListWithExistIds() {
		//given
		final List<Long> subcategoriesIds = Arrays.asList(1L, 2L, 3L);
		final List<Subcategory> subcategoriesList = Arrays.asList(subcategory1, subcategory2, subcategory3);

		when(subcategoryRepository.findAllById(subcategoriesIds)).thenReturn(subcategoriesList);

		//when
		List<Subcategory> result = subcategoryService.getSubcategoryList(subcategoriesIds);

		//then
		assertNotNull(result);
		assertEquals(3, result.size());

		assertEquals(SubcategoryName.SNACK.getKoreanName(), result.get(0).getName().getKoreanName());
		assertEquals(SubcategoryName.BEVERAGE.getKoreanName(), result.get(1).getName().getKoreanName());
		assertEquals(SubcategoryName.DRINKS.getKoreanName(), result.get(2).getName().getKoreanName());

		verify(subcategoryRepository, times(1)).findAllById(subcategoriesIds);
	}

	@Test
	@DisplayName("Query id가 Long type으로 입력되고 subcategory가 존재하지 않는 경우")
	public void testGetSubcategoryListWithNotExistIds() {
		//given
		final List<Long> subcategoriesIds = Arrays.asList(1L, 2L, 3L);

		//when
		when(subcategoryRepository.findAllById(subcategoriesIds)).thenThrow(SubcategoryNotFoundException.class);

		//then
		assertThrows(SubcategoryNotFoundException.class, () -> {
			subcategoryService.getSubcategoryList(subcategoriesIds);
		});
		verify(subcategoryRepository, times(1)).findAllById(subcategoriesIds);
	}
}
