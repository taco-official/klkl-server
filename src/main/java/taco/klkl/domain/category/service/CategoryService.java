package taco.klkl.domain.category.service;

import java.util.List;

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

@Service
public interface CategoryService {

	List<CategoryResponse> getCategories();

	CategoryWithSubcategoryResponse getSubcategories(Long id);

	List<CategoryResponse> getCategoriesByCategoryNames(List<CategoryName> categoryNames);
}
