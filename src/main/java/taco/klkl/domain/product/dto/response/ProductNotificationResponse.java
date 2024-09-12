package taco.klkl.domain.product.dto.response;

import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.product.domain.Product;

public record ProductNotificationResponse(
	Long id,
	ImageResponse image,
	String name
) {
	public static ProductNotificationResponse from(final Product product) {
		return new ProductNotificationResponse(
			product.getId(),
			ImageResponse.from(product.getMainImage()),
			product.getName()
		);
	}
}
