package taco.klkl.domain.product.dto.response;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public record PagedResponseDto<T>(
	List<T> content,
	int pageNumber,
	int pageSize,
	long totalElements,
	int totalPages,
	boolean last
) {

	public static <T, R> PagedResponseDto<R> of(Page<T> page, Function<T, R> mapper) {
		List<R> content = page.getContent().stream()
			.map(mapper)
			.toList();

		return new PagedResponseDto<>(
			content,
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.isLast()
		);
	}
}
