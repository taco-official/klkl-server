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

	public List<Subcategory> getSubcategoryList(List<String> subcategoryQuery) {
		List<Long> subcategoryIdList = parseSubcategoryQueryDto(subcategoryQuery);
		List<Subcategory> subcategories = subcategoryRepository.findAllById(subcategoryIdList);
		validateSubcategories(subcategoryQuery.size(), subcategories.size());
		return subcategories;
	}

	private List<Long> parseSubcategoryQueryDto(List<String> subcategoryQuery) {
		try {
			return subcategoryQuery.stream()
				.map(Long::parseLong)
				.toList();
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid subcategory ID format: " + e.getMessage());
		}
	}

	private void validateSubcategories(final int request, final int result) {
		if (request != result) {
			throw new SubcategoryNotFoundException();
		}
	}
}
