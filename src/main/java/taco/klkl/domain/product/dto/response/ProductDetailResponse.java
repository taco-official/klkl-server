package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.dto.response.tag.TagResponse;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductImage;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;
import taco.klkl.domain.user.dto.response.UserDetailResponse;
import taco.klkl.global.common.constants.DefaultConstants;
import taco.klkl.global.util.ProductUtil;

public record ProductDetailResponse(
	Long id,
	List<ImageResponse> images,
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
	@JsonFormat(pattern = DefaultConstants.DEFAULT_DATETIME_FORMAT) LocalDateTime createdAt
) {
	public static ProductDetailResponse from(final Product product) {
		final List<ImageResponse> images = product.getImages().stream()
			.map(ProductImage::getImage)
			.map(ImageResponse::from)
			.toList();

		return new ProductDetailResponse(
			product.getId(),
			images,
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
