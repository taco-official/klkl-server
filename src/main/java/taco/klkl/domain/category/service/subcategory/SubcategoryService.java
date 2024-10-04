package taco.klkl.domain.category.service.subcategory;

import java.util.List;

import taco.klkl.domain.category.dto.response.subcategory.SubcategoryHierarchyResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;

public interface SubcategoryService {

	List<SubcategorySimpleResponse> findAllSubcategoriesByPartialString(final String partialString);

	SubcategoryHierarchyResponse findSubcategoryHierarchyById(final Long id);
}
