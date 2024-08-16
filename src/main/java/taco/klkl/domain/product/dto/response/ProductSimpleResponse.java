package taco.klkl.domain.product.dto.response;

import java.util.Set;

import taco.klkl.domain.category.dto.response.FilterResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.global.util.ProductUtil;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * @param id
 * @param name
 * @param likeCount
 * @param rating
 * @param countryName
 * @param categoryName
 */
public record ProductSimpleResponse(
	Long id,
	String name,
	Integer likeCount,
	Double rating,
	String countryName,
	String categoryName,
	Set<FilterResponse> filters
) {

	public static ProductSimpleResponse from(final Product product) {
		return new ProductSimpleResponse(
			product.getId(),
			product.getName(),
			product.getLikeCount(),
			product.getRating().getValue(),
			product.getCity().getCountry().getName().getKoreanName(),
			product.getSubcategory().getCategory().getName().getKoreanName(),
			ProductUtil.createFiltersByProduct(product)
		);
	}
}
