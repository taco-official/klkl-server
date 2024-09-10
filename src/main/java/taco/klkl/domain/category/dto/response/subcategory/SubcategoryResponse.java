package taco.klkl.domain.category.dto.response.subcategory;

import java.util.List;

import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.tag.TagResponse;

public record SubcategoryResponse(
	Long id,
	String name,
	List<TagResponse> tags
) {
	public static SubcategoryResponse from(final Subcategory subcategory) {
		final List<TagResponse> tags = subcategory.getSubcategoryTags().stream()
			.map(SubcategoryTag::getTag)
			.map(TagResponse::from)
			.toList();

		return new SubcategoryResponse(
			subcategory.getId(),
			subcategory.getName(),
			tags
		);
	}
}
