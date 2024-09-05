package taco.klkl.domain.product.dto.response;

import java.util.Set;

import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.global.util.ProductUtil;

public record ProductSimpleResponse(
	Long id,
	String mainImageUrl,
	String name,
	Integer likeCount,
	Double rating,
	String countryName,
	String categoryName,
	Set<TagResponse> tags
) {
	public static ProductSimpleResponse from(final Product product) {
		return new ProductSimpleResponse(
			product.getId(),
			product.getMainImageUrl(),
			product.getName(),
			product.getLikeCount(),
			product.getRating().getValue(),
			product.getCity().getCountry().getName(),
			product.getSubcategory().getCategory().getName(),
			ProductUtil.generateTagsByProduct(product)
		);
	}
}
