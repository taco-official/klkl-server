package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;
import taco.klkl.domain.category.dto.response.tag.TagSimpleResponse;
import taco.klkl.domain.image.dto.response.ImageResponse;
import taco.klkl.domain.member.dto.response.MemberDetailResponse;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductImage;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;
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
	MemberDetailResponse member,
	CitySimpleResponse city,
	SubcategorySimpleResponse subcategory,
	CurrencyResponse currency,
	Set<TagSimpleResponse> tags,
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
			MemberDetailResponse.from(product.getMember()),
			CitySimpleResponse.from(product.getCity()),
			SubcategorySimpleResponse.from(product.getSubcategory()),
			CurrencyResponse.from(product.getCurrency()),
			ProductUtil.generateTagsByProduct(product),
			product.getCreatedAt()
		);
	}
}
