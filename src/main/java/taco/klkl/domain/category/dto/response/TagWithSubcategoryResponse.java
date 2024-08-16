package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryTag;

public record TagWithSubcategoryResponse(
	Long subcategoryId,
	String subcategory,
	List<TagResponse> tags
) {
	public static TagWithSubcategoryResponse from(Subcategory subcategory) {
		return new TagWithSubcategoryResponse(subcategory.getId(), subcategory.getName().getKoreanName(),
			subcategory.getSubcategoryTags()
				.stream()
				.map(SubcategoryTag::getTag)
				.map(TagResponse::from)
				.toList());
	}
}
