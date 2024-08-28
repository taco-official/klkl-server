package taco.klkl.domain.product.dto.response;

import java.util.List;
import java.util.Set;

import taco.klkl.domain.category.dto.response.TagResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.global.util.ProductUtil;

public record ProductSimpleResponse(
	Long id,
	List<ProductImageResponse> images,
	String name,
	Integer likeCount,
	Double rating,
	String countryName,
	String categoryName,
	Set<TagResponse> tags
) {
	public static ProductSimpleResponse from(final Product product) {
		List<ProductImageResponse> images = product.getImages().stream()
			.map(ProductImageResponse::from)
			.toList();

		return new ProductSimpleResponse(
			product.getId(),
			images,
			product.getName(),
			product.getLikeCount(),
			product.getRating().getValue(),
			product.getCity().getCountry().getName().getKoreanName(),
			product.getSubcategory().getCategory().getName().getKoreanName(),
			ProductUtil.createTagsByProduct(product)
		);
	}
}
