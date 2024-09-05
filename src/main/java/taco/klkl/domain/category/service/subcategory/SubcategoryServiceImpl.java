package taco.klkl.domain.category.service.subcategory;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.subcategory.SubcategoryRepository;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryResponse;
import taco.klkl.domain.category.exception.subcategory.SubcategoryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {

	private final SubcategoryRepository subcategoryRepository;

	@Override
	public List<SubcategoryResponse> findAllSubcategoriesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		final List<Subcategory> subcategories = subcategoryRepository.findAllByNameContaining(partialString);
		return subcategories.stream()
			.map(SubcategoryResponse::from)
			.toList();
	}

	@Override
	public List<Subcategory> findSubcategoriesBySubcategoryIds(final List<Long> subcategoryIds) {
		final List<Subcategory> subcategories = subcategoryRepository.findAllById(subcategoryIds);
		validateSubcategories(subcategoryIds.size(), subcategories.size());
		return subcategories;
	}

	private void validateSubcategories(final int request, final int result) {
		if (request != result) {
			throw new SubcategoryNotFoundException();
		}
	}
}
