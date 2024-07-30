package taco.klkl.domain.subcategory.sevice;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.subcategory.dao.SubcategoryRepository;
import taco.klkl.domain.subcategory.domain.Subcategory;
import taco.klkl.domain.subcategory.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.subcategory.exception.CategoryNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubcategoryService {
	private final SubcategoryRepository subcategoryRepository;

	public List<SubcategoryResponseDto> getSubcategories(Long id) {
		List<Subcategory> subcategories = subcategoryRepository.findAllByCategoryId(id);
		if (subcategories.isEmpty()) {
			throw new CategoryNotFoundException();
		}
		return subcategories.stream()
			.map(subcategory -> SubcategoryResponseDto.of(subcategory.getId(), subcategory.getName()))
			.toList();
	}
}
