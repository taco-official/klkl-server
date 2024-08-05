package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.product.domain.Product;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param productId
 * @param userId
 * @param name
 * @param description
 * @param address
 * @param likeCount
 * @param createdAt
 * @param price
 * @param cityId
 * @param subcategoryId
 * @param currencyId
 */
public record ProductDetailResponseDto(
	Long productId,
	Long userId,
	String name,
	String description,
	String address,
	int likeCount,
	LocalDateTime createdAt,
	int price,
	Long cityId,
	Long subcategoryId,
	Long currencyId
) {

	public static ProductDetailResponseDto from(Product product) {
		return new ProductDetailResponseDto(
			product.getProductId(),
			product.getUser().getId(),
			product.getName(),
			product.getDescription(),
			product.getAddress(),
			product.getLikeCount(),
			product.getCreatedAt(),
			product.getPrice(),
			product.getCity().getCityId(),
			product.getSubcategory().getId(),
			product.getCurrencyId()
		);
	}

}
