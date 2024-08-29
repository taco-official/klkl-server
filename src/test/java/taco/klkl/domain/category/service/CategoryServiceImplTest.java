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
import taco.klkl.domain.category.dao.category.CategoryRepository;
import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.category.CategoryResponse;
import taco.klkl.domain.category.exception.category.CategoryNotFoundException;
import taco.klkl.domain.category.service.category.CategoryServiceImpl;

@Transactional
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Mock
	private CategoryRepository categoryRepository;

	private final Category category = Category.of(CategoryType.FOOD);
	private final Category category2 = Category.of(CategoryType.CLOTHES);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryType.SNACK);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryType.INSTANT_FOOD);
	private final List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);

	@Test
	@DisplayName("카테고리 Service CategoryResponse(DTO)에 담겨 나오는지 Test")
	void testFindAllCategories() {
		// given
		Category category1 = Category.of(CategoryType.CLOTHES);
		Category category2 = Category.of(CategoryType.FOOD);
		List<Category> categories = Arrays.asList(category1, category2);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryResponse> result = categoryService.findAllCategories();

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals(CategoryType.CLOTHES.getName(), result.get(0).name());
		assertEquals(CategoryType.FOOD.getName(), result.get(1).name());

		verify(categoryRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Valid한 카테고리ID 입력시 해당하는 서브카테고리를 반환하는지 테스트")
	void testFindSubCategoriesByCategoryIdWithValidCategoryId() {
		//given
		Long categoryId = 1L;
		Category mockCategory = mock(Category.class);

		//when
		when(mockCategory.getSubcategories()).thenReturn(subcategories);
		when(mockCategory.getName()).thenReturn(CategoryType.FOOD.getName());
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));
		CategoryResponse response = categoryService.findSubCategoriesByCategoryId(categoryId);

		//then
		assertNotNull(response);
		assertEquals(SubcategoryType.SNACK.getName(), response.subcategories().get(0).name());
		assertEquals(SubcategoryType.INSTANT_FOOD.getName(), response.subcategories().get(1).name());

		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Invalid한 카테고리 ID 입력시 CategoryNotFoundException을 반환하는지 테스트")
	void testFindSubCategoriesByCategoryIdWithInvalidCategoryId() {
		//given
		Long categoryId = 1L;

		//when
		when(categoryRepository.findById(categoryId)).thenThrow(CategoryNotFoundException.class);

		//then
		assertThrows(CategoryNotFoundException.class, () -> {
			categoryService.findSubCategoriesByCategoryId(categoryId);
		});

		verify(categoryRepository, times(1)).findById(categoryId);
	}

	@Test
	@DisplayName("CategoryName리스트로 Category 조회")
	void testFindAllCategoriesByPartialString() {
		// given
		String partialName = "foo";
		List<Category> categories = Arrays.asList(category, category2);
		CategoryResponse category1ResponseDto = CategoryResponse.from(category);
		CategoryResponse category2ResponseDto = CategoryResponse.from(category2);

		when(categoryRepository.findAllByNameLike(partialName)).thenReturn(categories);

		// when
		List<CategoryResponse> categoryResponses = categoryService.findAllCategoriesByPartialString(partialName);

		// then
		Assertions.assertThat(categoryResponses.size()).isEqualTo(2);
		Assertions.assertThat(categoryResponses).containsExactly(category1ResponseDto, category2ResponseDto);
	}
}
