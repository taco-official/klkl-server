package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.dto.response.TagWithSubcategoryResponse;

@Service
public interface SubcategoryTagService {

	List<TagWithSubcategoryResponse> getTagsBySubcategoryList(List<Subcategory> subcategoryList);

}
