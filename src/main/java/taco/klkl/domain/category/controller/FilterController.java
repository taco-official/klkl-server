package taco.klkl.domain.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.dto.response.SubcategoryWithFilterResponseDto;
import taco.klkl.domain.category.service.SubcategoryFilterService;
import taco.klkl.domain.category.service.SubcategoryService;

@Slf4j
@Tag(name = "7. 서브카테고리", description = "서브카테고리 관련 API")
@RestController
@RequestMapping("/v1/filters")
@RequiredArgsConstructor
public class FilterController {
	private final SubcategoryService subcategoryService;
	private final SubcategoryFilterService subcategoryFilterService;

	@GetMapping
	@Operation(description = "Subcategory 포함된 Filter 반환")
	public ResponseEntity<List<SubcategoryWithFilterResponseDto>> getFilter(
		@RequestParam("subcategories") List<Long> subcategoryIds) {
		List<Subcategory> subcategoryList = subcategoryService.getSubcategoryList(subcategoryIds);
		List<SubcategoryWithFilterResponseDto> subcategoryWithFilterResponseDto = subcategoryFilterService.getFilters(
			subcategoryList);
		return ResponseEntity.ok(subcategoryWithFilterResponseDto);
	}
}
