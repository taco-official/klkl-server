package taco.klkl.domain.category.service.category;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.category.CategoryRepository;
import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.dto.response.category.CategoryResponse;
import taco.klkl.domain.category.exception.category.CategoryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryResponse> findAllCategories() {
		final List<Category> categories = categoryRepository.findAll();
		return categories.stream()
			.map(CategoryResponse::from)
			.toList();
	}

	@Override
	public List<CategoryResponse> findAllCategoriesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		final List<Category> categories = categoryRepository.findAllByNameContaining(partialString);
		return categories.stream()
			.map(CategoryResponse::from)
			.toList();
	}
}
