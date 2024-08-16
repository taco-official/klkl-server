package taco.klkl.domain.category.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.FilterRepository;
import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.exception.FilterNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FilterService {

	private final FilterRepository filterRepository;

	public Filter getFilterEntityById(final Long id) {
		return filterRepository.findById(id)
			.orElseThrow(FilterNotFoundException::new);
	}
}
