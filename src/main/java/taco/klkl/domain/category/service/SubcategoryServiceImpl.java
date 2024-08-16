package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.SubcategoryRepository;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.SubcategoryResponse;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {

	private final SubcategoryRepository subcategoryRepository;

	@Override
	public List<SubcategoryResponse> getSubcategoriesBySubcategoryNames(List<SubcategoryName> subcategoryNames) {

		if (subcategoryNames == null || subcategoryNames.isEmpty()) {
			return List.of();
		}

		List<Subcategory> subcategories = subcategoryRepository.findAllByNameIn(subcategoryNames);

		return subcategories.stream()
			.map(SubcategoryResponse::from)
			.toList();
	}

	@Override
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
