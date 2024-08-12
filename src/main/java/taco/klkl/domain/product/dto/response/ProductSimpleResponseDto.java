package taco.klkl.domain.product.dto.response;

import taco.klkl.domain.product.domain.Product;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * @param productId
 * @param name
 * @param likeCount
 * @param countryName
 * @param categoryName
 */
public record ProductSimpleResponseDto(
	Long productId,
	String name,
	int likeCount,
	String countryName,
	String categoryName
) {

	public static ProductSimpleResponseDto from(Product product) {
		return new ProductSimpleResponseDto(
			product.getId(),
			product.getName(),
			product.getLikeCount(),
			product.getCity().getCountry().getName().getKoreanName(),
			product.getSubcategory().getCategory().getName().getKoreanName()
		);
	}
}
