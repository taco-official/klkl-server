package taco.klkl.domain.category.dto.response;

import java.util.List;

import taco.klkl.domain.category.domain.Category;

public record CategoryWithSubcategoryDto(
	Long categoryId,
	String category,
	List<SubcategoryResponseDto> subcategories
) {
	public static CategoryWithSubcategoryDto from(Category category) {
		return new CategoryWithSubcategoryDto(category.getId(), category.getName().getKoreanName(),
			category.getSubcategories()
				.stream()
				.map(subcategory -> SubcategoryResponseDto.from(subcategory))
				.toList());
	}
}
