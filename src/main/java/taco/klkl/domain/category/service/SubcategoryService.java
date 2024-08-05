package taco.klkl.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dao.SubcategoryRepository;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubcategoryService {
	private final SubcategoryRepository subcategoryRepository;

	public List<SubcategoryResponseDto> getSubcategoriesBySubcategoryNames(List<SubcategoryName> subcategoryNames) {
		List<Subcategory> findCategories = subcategoryRepository.findAllByNameIn(subcategoryNames);

		return findCategories.stream()
			.map(SubcategoryResponseDto::from)
			.toList();
	}
}
