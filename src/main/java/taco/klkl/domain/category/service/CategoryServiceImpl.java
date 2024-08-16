package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.CategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryResponse;
import taco.klkl.domain.category.exception.CategoryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryResponse> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream()
			.map(CategoryResponse::from)
			.toList();
	}

	@Override
	public CategoryWithSubcategoryResponse getSubcategories(Long id) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(CategoryNotFoundException::new);
		return CategoryWithSubcategoryResponse.from(category);
	}

	@Override
	public List<CategoryResponse> getCategoriesByCategoryNames(List<CategoryName> categoryNames) {

		if (categoryNames == null || categoryNames.isEmpty()) {
			return List.of();
		}

		List<Category> categories = categoryRepository.findAllByNameIn(categoryNames);

		return categories.stream()
			.map(CategoryResponse::from)
			.toList();
	}
}
