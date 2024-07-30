package taco.klkl.domain.product.dto.request;

public record ProductUpdateRequestDto(
	String name,
	String description,
	Long cityId,
	Long subcategoryId,
	Long currencyId,
	String address,
	Integer price
) {
}
