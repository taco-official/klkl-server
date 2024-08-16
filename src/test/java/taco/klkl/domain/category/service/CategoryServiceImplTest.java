package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.transaction.Transactional;
import taco.klkl.domain.category.dao.CategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryResponse;
import taco.klkl.domain.category.exception.CategoryNotFoundException;

@Transactional
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Mock
	private CategoryRepository categoryRepository;

	private final Category category = Category.of(CategoryName.FOOD);
	private final Category category2 = Category.of(CategoryName.CLOTHES);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryName.DRESS);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryName.HAIR_CARE);
	private final List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);

	@Test
	@DisplayName("카테고리 Service CategoryResponse(DTO)에 담겨 나오는지 Test")
	void testGetCategories() {
		// given
		Category category1 = Category.of(CategoryName.CLOTHES);
		Category category2 = Category.of(CategoryName.FOOD);
		List<Category> categories = Arrays.asList(category1, category2);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryResponse> result = categoryService.getCategories();

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals(CategoryName.CLOTHES.getKoreanName(), result.get(0).category());
		assertEquals(CategoryName.FOOD.getKoreanName(), result.get(1).category());

		verify(categoryRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Valid한 카테고리ID 입력시 해당하는 서브카테고리를 반환하는지 테스트")
	void testGetSubcategoriesWithValidCategoryId() {
		//given
		Long categoryId = 1L;
		Category mockCategory = mock(Category.class);

		//when
		when(mockCategory.getSubcategories()).thenReturn(subcategories);
		when(mockCategory.getName()).thenReturn(CategoryName.FOOD);
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));
		CategoryWithSubcategoryResponse response = categoryService.getSubcategories(categoryId);

		//then
		assertNotNull(response);
		assertEquals(SubcategoryName.DRESS.getKoreanName(), response.subcategories().get(0).subcategory());
		assertEquals(SubcategoryName.HAIR_CARE.getKoreanName(), response.subcategories().get(1).subcategory());

		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Invalid한 카테고리 ID 입력시 CategoryNotFoundException을 반환하는지 테스트")
	void testGetSubcategoriesWithInvalidCategoryId() {
		//given
		Long categoryId = 1L;

		//when
		when(categoryRepository.findById(categoryId)).thenThrow(CategoryNotFoundException.class);

		//then
		assertThrows(CategoryNotFoundException.class, () -> {
			categoryService.getSubcategories(categoryId);
		});

		verify(categoryRepository, times(1)).findById(categoryId);
	}

	@Test
	@DisplayName("CategoryName리스트로 Category 조회")
	void testGetCategoriesByCategoryNames() {
		// given
		List<CategoryName> categoryNames = Arrays.asList(CategoryName.CLOTHES, CategoryName.FOOD);
		List<Category> categories = Arrays.asList(category, category2);
		CategoryResponse category1ResponseDto = CategoryResponse.from(category);
		CategoryResponse category2ResponseDto = CategoryResponse.from(category2);

		when(categoryRepository.findAllByNameIn(categoryNames)).thenReturn(categories);

		// when
		List<CategoryResponse> categoryResponses = categoryService.getCategoriesByCategoryNames(categoryNames);

		// then
		Assertions.assertThat(categoryResponses.size()).isEqualTo(categoryNames.size());
		Assertions.assertThat(categoryResponses).containsExactly(category1ResponseDto, category2ResponseDto);
	}
}
