package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.SubcategoryRepository;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryService {
	private final SubcategoryRepository subcategoryRepository;

	public List<SubcategoryResponseDto> getSubcategoriesBySubcategoryNames(List<SubcategoryName> subcategoryNames) {

		if (subcategoryNames == null || subcategoryNames.isEmpty()) {
			return List.of();
		}

		List<Subcategory> subcategories = subcategoryRepository.findAllByNameIn(subcategoryNames);

		return subcategories.stream()
			.map(SubcategoryResponseDto::from)
			.toList();
	}

	public List<Subcategory> getSubcategoryList(List<Long> subcategoryIds) {
		List<Subcategory> subcategories = subcategoryRepository.findAllById(subcategoryIds);
		validateSubcategories(subcategoryIds.size(), subcategories.size());
		return subcategories;
	}

	public Subcategory getSubcategoryById(Long id) {
		return subcategoryRepository.findById(id)
			.orElseThrow(SubcategoryNotFoundException::new);
	}

	private void validateSubcategories(final int request, final int result) {
		if (request != result) {
			throw new SubcategoryNotFoundException();
		}
	}
}
