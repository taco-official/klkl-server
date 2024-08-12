package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;

import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CurrencyResponseDto;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param productId
 * @param name
 * @param description
 * @param address
 * @param price
 * @param likeCount
 * @param user
 * @param city
 * @param subcategory
 * @param currency
 * @param createdAt
 */
public record ProductDetailResponseDto(
	Long productId,
	String name,
	String description,
	String address,
	int price,
	int likeCount,
	UserDetailResponseDto user,
	CityResponseDto city,
	SubcategoryResponseDto subcategory,
	CurrencyResponseDto currency,
	LocalDateTime createdAt
) {

	public static ProductDetailResponseDto from(Product product) {
		return new ProductDetailResponseDto(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getAddress(),
			product.getPrice(),
			product.getLikeCount(),
			UserDetailResponseDto.from(product.getUser()),
			CityResponseDto.from(product.getCity()),
			SubcategoryResponseDto.from(product.getSubcategory()),
			CurrencyResponseDto.from(product.getCurrency()),
			product.getCreatedAt()
		);
	}

}
