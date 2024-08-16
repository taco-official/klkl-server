package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryTag;

public record TagWithSubcategoryResponse(
	Long id,
	String name,
	List<TagResponse> tags
) {
	public static TagWithSubcategoryResponse from(final Subcategory subcategory) {
		return new TagWithSubcategoryResponse(
			subcategory.getId(),
			subcategory.getName().getKoreanName(),
			createTagsBySubcategory(subcategory)
		);
	}

	private static List<TagResponse> createTagsBySubcategory(final Subcategory subcategory) {
		return subcategory.getSubcategoryTags().stream()
			.map(SubcategoryTag::getTag)
			.map(TagResponse::from)
			.toList();
	}
}
