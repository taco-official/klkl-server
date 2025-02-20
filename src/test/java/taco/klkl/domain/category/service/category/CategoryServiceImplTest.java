package taco.klkl.domain.category.service.category;

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

import jakarta.transaction.Transactional;
import taco.klkl.domain.category.dao.category.CategoryRepository;
import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.dto.response.category.CategoryDetailResponse;
import taco.klkl.domain.category.dto.response.category.CategorySimpleResponse;

@Transactional
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Mock
	private CategoryRepository categoryRepository;

	private final Category category = Category.of(CategoryType.FOOD);
	private final Category category2 = Category.of(CategoryType.CLOTHES);

	@Test
	@DisplayName("CategoryDetailResponse 에 담겨 나오는지 Test")
	void testFindAllCategories() {
		// given
		Category category1 = Category.of(CategoryType.CLOTHES);
		Category category2 = Category.of(CategoryType.FOOD);
		List<Category> categories = Arrays.asList(category1, category2);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryDetailResponse> result = categoryService.findAllCategories();

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals(CategoryType.CLOTHES.getName(), result.get(0).name());
		assertEquals(CategoryType.FOOD.getName(), result.get(1).name());

		verify(categoryRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("이름 부분 문자열로 Category 조회")
	void testFindAllCategoriesByPartialString() {
		// given
		String partialName = "foo";
		List<Category> categories = Arrays.asList(category, category2);
		CategorySimpleResponse category1Response = CategorySimpleResponse.from(category);
		CategorySimpleResponse category2Response = CategorySimpleResponse.from(category2);

		when(categoryRepository.findAllByNameContaining(partialName)).thenReturn(categories);

		// when
		List<CategorySimpleResponse> categorySimpleResponses =
				categoryService.findAllCategoriesByPartialString(partialName);

		// then
		Assertions.assertThat(categorySimpleResponses.size()).isEqualTo(2);
		Assertions.assertThat(categorySimpleResponses).containsExactly(category1Response, category2Response);
	}
}
