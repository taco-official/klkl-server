package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dao.subcategory.SubcategoryRepository;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.exception.subcategory.SubcategoryNotFoundException;

@Component
@RequiredArgsConstructor
public class SubcategoryUtil {

	private final SubcategoryRepository subcategoryRepository;

	public Subcategory findSubcategoryEntityById(final Long id) {
		return subcategoryRepository.findById(id)
			.orElseThrow(SubcategoryNotFoundException::new);
	}
}
