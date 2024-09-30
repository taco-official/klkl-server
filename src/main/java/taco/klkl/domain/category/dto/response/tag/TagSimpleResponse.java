package taco.klkl.domain.category.dto.response.tag;

import taco.klkl.domain.category.domain.tag.Tag;

public record TagSimpleResponse(
	Long id,
	String name
) {
	public static TagSimpleResponse from(final Tag tag) {
		return new TagSimpleResponse(tag.getId(), tag.getName());
	}
}
