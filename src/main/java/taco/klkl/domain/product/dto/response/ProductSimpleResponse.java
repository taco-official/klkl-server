package taco.klkl.domain.product.dto.response;

import java.util.Set;

import taco.klkl.domain.category.dto.response.tag.TagSimpleResponse;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.global.util.ProductUtil;

public record ProductSimpleResponse(
	Long id,
	ImageResponse image,
	String name,
	int likeCount,
	double rating,
	String countryName,
	String categoryName,
	Set<TagSimpleResponse> tags,
	boolean isLiked
) {
	public static ProductSimpleResponse from(final Product product, final boolean isLiked) {
		return new ProductSimpleResponse(
			product.getId(),
			ImageResponse.from(product.getMainImage()),
			product.getName(),
			product.getLikeCount(),
			product.getRating().getValue(),
			product.getCity().getCountry().getName(),
			product.getSubcategory().getCategory().getName(),
			ProductUtil.generateTagsByProduct(product),
			isLiked
		);
	}
}
