package taco.klkl.domain.product.dto.response;

import jakarta.validation.constraints.NotNull;
import taco.klkl.domain.product.domain.Product;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * @param productId
 * @param name
 * @param description
 * @param likeCount
 * @param cityId
 * @param subcategoryId
 * @param currencyId
 */
public record ProductSimpleResponseDto(
	@NotNull(message = "productId is required") Long productId,
	@NotNull(message = "name is required") String name,
	@NotNull(message = "description is required") String description,
	@NotNull(message = "likeCount is required") Long likeCount,
	@NotNull(message = "cityId is required") Long cityId,
	@NotNull(message = "subcategoryId is required") Long subcategoryId,
	@NotNull(message = "currencyId is required") Long currencyId
) {

	public ProductSimpleResponseDto from(Product product) {
		return new ProductSimpleResponseDto(
			product.getProductId(),
			product.getName(),
			product.getDescription(),
			product.getLikeCount(),
			product.getCityId(),
			product.getSubcategoryId(),
			product.getCurrencyId()
		);
	}
}
