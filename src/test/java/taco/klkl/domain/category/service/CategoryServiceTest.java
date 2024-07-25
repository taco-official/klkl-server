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

import jakarta.transaction.Transactional;
import taco.klkl.domain.category.dao.CategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.dto.response.CategoryResponse;

@ExtendWith(MockitoExtension.class)
@Transactional
class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@Test
	@DisplayName("카테고리 Service CategoryResponse(DTO)에 담겨 나오는지 Test")
	void testGetCategories() {
		// given
		Category category1 = Category.of("Category 1");
		Category category2 = Category.of("Category 2");
		List<Category> categories = Arrays.asList(category1, category2);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryResponse> result = categoryService.getCategories();
		System.out.println(categories.get(0).getName());

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals("Category 1", result.get(0).name());
		assertEquals("Category 2", result.get(1).name());

		verify(categoryRepository, times(1)).findAll();
	}
}
