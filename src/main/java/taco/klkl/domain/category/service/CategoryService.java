package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.CategoryType;
import taco.klkl.domain.category.dto.response.CategoryResponse;

@Service
public interface CategoryService {

	List<CategoryResponse> findAllCategories();

	CategoryResponse findSubCategoriesByCategoryId(final Long categoryId);

	List<CategoryResponse> findAllCategoriesByPartialString(final String partialString);
}
