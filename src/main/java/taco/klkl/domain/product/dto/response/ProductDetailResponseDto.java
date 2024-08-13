package taco.klkl.domain.product.dto.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductFilter;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CurrencyResponseDto;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품 컨트롤러에서 필터 서비스를 이용해서 조합 하는게 괜찮아 보입니다.
 * @param id
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
	Long id,
	String name,
	String description,
	String address,
	int price,
	int likeCount,
	UserDetailResponseDto user,
	CityResponseDto city,
	SubcategoryResponseDto subcategory,
	CurrencyResponseDto currency,
	Set<FilterResponseDto> filters,
	LocalDateTime createdAt
) {

	public static ProductDetailResponseDto from(Product product) {
		Set<FilterResponseDto> filters = Optional.ofNullable(product.getFilters())
			.map(productFilters -> productFilters.stream()
				.map(ProductFilter::getFilter)
				.filter(Objects::nonNull)
				.map(FilterResponseDto::from)
				.collect(Collectors.toSet()))
			.orElse(Collections.emptySet());

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
			filters,
			product.getCreatedAt()
		);
	}

}
