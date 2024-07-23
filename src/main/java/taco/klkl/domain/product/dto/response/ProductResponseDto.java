package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.user.domain.User;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param productId
 * @param user
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
public record ProductResponseDto(
	@NotNull(message = "productId is required") Long productId,
	@NotNull(message = "user is required") User user,
	@NotNull(message = "name is required") String name,
	@NotNull(message = "description is required") String description,
	@NotNull(message = "address is required") String address,
	@NotNull(message = "likeCount is required") Long likeCount,
	@NotNull(message = "createdAt is required") LocalDateTime createdAt,
	@NotNull(message = "price is required") Long price,
	@NotNull(message = "cityId is required") Long cityId,
	@NotNull(message = "subcategoryId is required") Long subcategoryId,
	@NotNull(message = "currencyId is required") Long currencyId
) {

	public ProductResponseDto from(Product product) {
		return new ProductResponseDto(
			product.getProductId(),
			product.getUser(),
			product.getName(),
			product.getDescription(),
			product.getAddress(),
			product.getLikeCount(),
			product.getCreatedAt(),
			product.getPrice(),
			product.getCityId(),
			product.getSubcategoryId(),
			product.getCurrencyId()
		);
	}

}
