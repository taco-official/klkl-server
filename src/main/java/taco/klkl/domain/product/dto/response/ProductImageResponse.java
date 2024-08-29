package taco.klkl.domain.product.dto.response;

import taco.klkl.domain.product.domain.ProductImage;

public record ProductImageResponse(
	String url,
	Integer orderIndex
) {
	public static ProductImageResponse from(final ProductImage productImage) {
		return new ProductImageResponse(productImage.getImageUrl(), productImage.getOrderIndex());
	}
}
