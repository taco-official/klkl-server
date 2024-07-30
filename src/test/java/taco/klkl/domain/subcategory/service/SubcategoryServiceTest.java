package taco.klkl.domain.subcategory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.transaction.Transactional;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.subcategory.dao.SubcategoryRepository;
import taco.klkl.domain.subcategory.domain.Subcategory;
import taco.klkl.domain.subcategory.domain.SubcategoryName;
import taco.klkl.domain.subcategory.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.subcategory.exception.CategoryNotFoundException;
import taco.klkl.domain.subcategory.sevice.SubcategoryService;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SubcategoryServiceTest {

	@Mock
	private SubcategoryRepository subcategoryRepository;

	@InjectMocks
	private SubcategoryService subcategoryService;

	@Test
	@DisplayName("Valid한 카테고리ID 입력시 해당하는 서브카테고리를 반환하는지 테스트")
	void testGetSubcategoriesWithValidCategoryId() {
		//given
		Category category = Category.of("Category1");
		Subcategory subcategory1 = Subcategory.of(category, SubcategoryName.DRESS);
		Subcategory subcategory2 = Subcategory.of(category, SubcategoryName.HAIR_CARE);
		List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);

		//when
		when(subcategoryRepository.findAllByCategoryId(category.getId())).thenReturn(subcategories);
		List<SubcategoryResponseDto> response = subcategoryService.getSubcategories(category.getId());

		//then
		assertNotNull(response);
		assertEquals(2, response.size());
		assertEquals(SubcategoryName.DRESS.getName(), response.get(0).name());
		assertEquals(SubcategoryName.HAIR_CARE.getName(), response.get(1).name());

		verify(subcategoryRepository, times(1)).findAllByCategoryId(category.getId());
	}

	@Test
	@DisplayName("Invalid한 카테고리 ID 입력시 CategoryNotFoundException을 반환하는지 테스트")
	void testGetSubcategoriesWithInvalidCategoryId() {
		//given
		Long categoryId = 1L;

		//when
		when(subcategoryRepository.findAllByCategoryId(categoryId)).thenReturn(Collections.emptyList());

		//then
		assertThrows(CategoryNotFoundException.class, () -> {
			subcategoryService.getSubcategories(categoryId);
		});

		verify(subcategoryRepository, times(1)).findAllByCategoryId(categoryId);
	}
}

