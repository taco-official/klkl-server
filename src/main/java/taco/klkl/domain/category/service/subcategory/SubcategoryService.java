package taco.klkl.domain.category.service.subcategory;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryHierarchyResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;

@Service
public interface SubcategoryService {

	List<SubcategoryResponse> findAllSubcategoriesByPartialString(final String partialString);

	List<Subcategory> findSubcategoriesBySubcategoryIds(final List<Long> subcategoryIds);

	SubcategoryHierarchyResponse findSubcategoryHierarchyById(final Long id);
}
