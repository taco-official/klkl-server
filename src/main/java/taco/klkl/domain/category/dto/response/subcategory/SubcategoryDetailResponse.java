package taco.klkl.domain.category.dto.response.subcategory;

import java.util.List;

import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.tag.TagSimpleResponse;

public record SubcategoryDetailResponse(
	Long id,
	String name,
	List<TagSimpleResponse> tags
) {
	public static SubcategoryDetailResponse from(final Subcategory subcategory) {
		final List<TagSimpleResponse> tags = subcategory.getSubcategoryTags().stream()
			.map(SubcategoryTag::getTag)
			.map(TagSimpleResponse::from)
			.toList();

		return new SubcategoryDetailResponse(
			subcategory.getId(),
			subcategory.getName(),
			tags
		);
	}
}
