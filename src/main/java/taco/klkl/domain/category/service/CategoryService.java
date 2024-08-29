package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponse;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryResponse;

@Service
public interface CategoryService {

	List<CategoryResponse> findAllCategories();

	CategoryWithSubcategoryResponse findSubCategoriesByCategoryId(final Long categoryId);

	List<CategoryResponse> findAllCategoriesByCategoryNames(final List<CategoryName> categoryNames);
}
