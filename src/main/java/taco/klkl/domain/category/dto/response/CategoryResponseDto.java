package taco.klkl.domain.category.dto.response;

public record CategoryResponseDto(
	Long id,
	String name
) {
	public static CategoryResponseDto of(Long id, String name) {
		return new CategoryResponseDto(id, name);
	}
}
