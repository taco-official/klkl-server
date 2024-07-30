package taco.klkl.domain.subcategory.dto.response;

import taco.klkl.domain.subcategory.domain.SubcategoryName;

public record SubcategoryResponseDto(
	Long id,
	String name
) {
	public static SubcategoryResponseDto of(Long id, SubcategoryName subcategoryName) {
		return new SubcategoryResponseDto(id, subcategoryName.getName());
	}
}
