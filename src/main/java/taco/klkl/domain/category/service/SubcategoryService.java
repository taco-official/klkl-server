package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.SubcategoryRepository;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryService {
	private final SubcategoryRepository subcategoryRepository;

	public List<Subcategory> getSubcategoryList(List<Long> subcategoryIds) {
		List<Subcategory> subcategories = subcategoryRepository.findAllById(subcategoryIds);
		validateSubcategories(subcategoryIds.size(), subcategories.size());
		return subcategories;
	}

	private void validateSubcategories(final int request, final int result) {
		if (request != result) {
			throw new SubcategoryNotFoundException();
		}
	}
}
