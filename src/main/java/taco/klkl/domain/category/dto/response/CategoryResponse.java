package taco.klkl.domain.category.dto.response;

public record CategoryResponse(
	Long id,
	String name
) {
	public static CategoryResponse of(Long id, String name) {
		return new CategoryResponse(id, name);
	}
}
