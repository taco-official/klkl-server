package taco.klkl.domain.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.service.SubcategoryService;

@Slf4j
@Tag(name = "7. 서브카테고리", description = "서브카테고리 관련 API")
@RestController
@RequestMapping("/v1/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {
	private final SubcategoryService subcategoryService;

	// @GetMapping("/{id}/filter")
	// public ResponseEntity<List<SubcategoryResponseDto>> getFilter(@PathVariable Long id) {
	// 	List<SubcategoryResponseDto> subcategories = subcategoryService.getSubcategories(id);
	// 	return ResponseEntity.ok(subcategories);
	// }
}
