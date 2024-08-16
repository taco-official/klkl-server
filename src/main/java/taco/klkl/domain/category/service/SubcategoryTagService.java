package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.dto.response.SubcategoryWithTagsResponse;

@Service
public interface SubcategoryTagService {

	List<SubcategoryWithTagsResponse> findSubcategoryTagsBySubcategoryList(final List<Subcategory> subcategoryList);

}
