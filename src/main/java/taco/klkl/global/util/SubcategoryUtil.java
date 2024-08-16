package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dao.SubcategoryRepository;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;

@Component
@RequiredArgsConstructor
public class SubcategoryUtil {

	private final SubcategoryRepository subcategoryRepository;

	public Subcategory getSubcategoryEntityById(final Long id) {
		return subcategoryRepository.findById(id)
			.orElseThrow(SubcategoryNotFoundException::new);
	}
}
