package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;

@Service
public interface SubcategoryService {

	List<SubcategoryResponse> getSubcategoriesBySubcategoryNames(List<SubcategoryName> subcategoryNames);

	List<Subcategory> getSubcategoryList(List<Long> subcategoryIds);
}
