package taco.klkl.global.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtil {

	public Pageable createPageableSortedByCreatedAtDesc(final Pageable pageable) {
		return PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(Sort.Direction.DESC, "createdAt")
		);
	}
}
