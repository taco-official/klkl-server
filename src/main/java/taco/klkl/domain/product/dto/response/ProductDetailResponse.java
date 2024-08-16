package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import taco.klkl.domain.category.dto.response.FilterResponse;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CurrencyResponse;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.util.ProductUtil;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param id
 * @param name
 * @param description
 * @param address
 * @param price
 * @param likeCount
 * @param rating
 * @param user
 * @param city
 * @param subcategory
 * @param currency
 * @param createdAt
 */
public record ProductDetailResponse(
	Long id,
	String name,
	String description,
	String address,
	Integer price,
	Integer likeCount,
	Double rating,
	UserDetailResponse user,
	CityResponse city,
	SubcategoryResponse subcategory,
	CurrencyResponse currency,
	Set<FilterResponse> filters,
	LocalDateTime createdAt
) {

	public static ProductDetailResponse from(final Product product) {
		return new ProductDetailResponse(
			product.getId(),
			product.getName(),
			product.getDescription(),
			product.getAddress(),
			product.getPrice(),
			product.getLikeCount(),
			product.getRating().getValue(),
			UserDetailResponse.from(product.getUser()),
			CityResponse.from(product.getCity()),
			SubcategoryResponse.from(product.getSubcategory()),
			CurrencyResponse.from(product.getCurrency()),
			ProductUtil.createFiltersByProduct(product),
			product.getCreatedAt()
		);
	}
}
