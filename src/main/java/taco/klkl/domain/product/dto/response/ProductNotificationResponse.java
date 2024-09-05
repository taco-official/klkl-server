package taco.klkl.domain.product.dto.response;

import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductImage;

public record ProductNotificationResponse(
	Long id,
	String productImageUrl,
	String name
) {
	public static ProductNotificationResponse from(final Product product) {
		final String productImageUrl = product.getImages().stream()
			.findFirst()
			.map(ProductImage::getImageUrl)
			.orElse(null);

		return new ProductNotificationResponse(
			product.getId(),
			productImageUrl,
			product.getName()
		);
	}
}
