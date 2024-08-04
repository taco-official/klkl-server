package taco.klkl.domain.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryDto;
import taco.klkl.domain.category.service.CategoryService;

@Slf4j
@RestController
@Tag(name = "6. 카테고리", description = "카테고리 관련 API")
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	@Operation(description = "전체 Category 반환")
	public ResponseEntity<List<CategoryResponseDto>> getCategory() {
		List<CategoryResponseDto> categoryResponseDto = categoryService.getCategories();
		return ResponseEntity.ok(categoryResponseDto);
	}

	@GetMapping("/{id}/subcategories")
	@Operation(description = "Category에 포함된 Subcategory 반환")
	public ResponseEntity<CategoryWithSubcategoryDto> getSubCategory(@PathVariable Long id) {
		CategoryWithSubcategoryDto categoryWithSubcategoryDto = categoryService.getSubcategories(id);
		return ResponseEntity.ok(categoryWithSubcategoryDto);
	}
}
