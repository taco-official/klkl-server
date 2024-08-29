package taco.klkl.domain.category.service.tag;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.tag.TagResponse;

@Service
public interface SubcategoryTagService {

	Set<TagResponse> findTagsBySubcategoryList(final List<Subcategory> subcategoryList);

}
