package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dao.FilterRepository;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.exception.FilterNotFoundException;

@Component
@RequiredArgsConstructor
public class FilterUtil {

	private final FilterRepository filterRepository;

	public Filter getFilterEntityById(final Long id) {
		return filterRepository.findById(id)
			.orElseThrow(FilterNotFoundException::new);
	}
}
