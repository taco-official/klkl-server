package taco.klkl.domain.category.service.category;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.dto.response.category.CategoryResponse;

@Service
public interface CategoryService {

	List<CategoryResponse> findAllCategories();

	List<CategoryResponse> findAllCategoriesByPartialString(final String partialString);
}
