package taco.klkl.domain.category.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.CategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.dto.response.CategoryResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<CategoryResponse> getCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream()
			.map(category -> CategoryResponse.of(category.getId(), category.getName()))
			.collect(Collectors.toList());
	}
}
