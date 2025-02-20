package taco.klkl.domain.category.service.subcategory;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.subcategory.SubcategoryRepository;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryHierarchyResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;
import taco.klkl.domain.category.exception.subcategory.SubcategoryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {

	private final SubcategoryRepository subcategoryRepository;

	@Override
	public List<SubcategorySimpleResponse> findAllSubcategoriesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		final List<Subcategory> subcategories = subcategoryRepository.findAllByNameContaining(partialString);
		return subcategories.stream()
			.map(SubcategorySimpleResponse::from)
			.toList();
	}

	@Override
	public SubcategoryHierarchyResponse findSubcategoryHierarchyById(final Long id) {
		final Subcategory subcategory = subcategoryRepository.findById(id)
			.orElseThrow(SubcategoryNotFoundException::new);
		return SubcategoryHierarchyResponse.from(subcategory);
	}
}
