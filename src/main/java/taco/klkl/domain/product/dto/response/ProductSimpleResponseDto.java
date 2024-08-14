package taco.klkl.domain.product.dto.response;

import java.util.Set;
import java.util.stream.Collectors;

import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductFilter;

/**
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * TODO: 상품필터속성 추가 해야함 (상품필터속성 테이블 개발 후)
 * @param id
 * @param name
 * @param likeCount
 * @param countryName
 * @param categoryName
 */
public record ProductSimpleResponseDto(
	Long id,
	String name,
	int likeCount,
	String countryName,
	String categoryName,
	Set<FilterResponseDto> filters
) {

	public static ProductSimpleResponseDto from(Product product) {
		Set<FilterResponseDto> filters = product.getFilters().stream()
			.map(ProductFilter::getFilter)
			.map(FilterResponseDto::from)
			.collect(Collectors.toSet());

		return new ProductSimpleResponseDto(
			product.getId(),
			product.getName(),
			product.getLikeCount(),
			product.getCity().getCountry().getName().getKoreanName(),
			product.getSubcategory().getCategory().getName().getKoreanName(),
			filters
		);
	}
}
