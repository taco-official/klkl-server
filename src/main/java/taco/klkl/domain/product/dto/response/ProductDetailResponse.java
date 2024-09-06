package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.util.ProductUtil;

public record ProductDetailResponse(
	Long id,
	List<String> imageUrls,
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
	Set<TagResponse> tags,
	LocalDateTime createdAt
) {
	public static ProductDetailResponse from(final Product product) {
		return new ProductDetailResponse(
			product.getId(),
			ProductUtil.generateImageUrlsByProduct(product),
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
			ProductUtil.generateTagsByProduct(product),
			product.getCreatedAt()
		);
	}
}
