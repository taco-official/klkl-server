package taco.klkl.domain.product.dto.response;

import taco.klkl.domain.product.domain.Product;

public record ProductNotificationResponse(
	Long id,
	String mainImageUrl,
	String name
) {
	public static ProductNotificationResponse from(final Product product) {
		return new ProductNotificationResponse(
			product.getId(),
			product.getMainImageUrl(),
			product.getName()
		);
	}
}
