package taco.klkl.domain.category.service.category;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.dto.response.category.CategoryDetailResponse;
import taco.klkl.domain.category.dto.response.category.CategorySimpleResponse;

@Service
public interface CategoryService {

	List<CategoryDetailResponse> findAllCategories();

	List<CategorySimpleResponse> findAllCategoriesByPartialString(final String partialString);
}
