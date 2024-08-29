package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Tag;

public record TagResponse(
	Long id,
	String name
) {
	public static TagResponse from(final Tag tag) {
		return new TagResponse(tag.getId(), tag.getName());
	}
}
