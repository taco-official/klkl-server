package taco.klkl.domain.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.service.CategoryService;

@Slf4j
@RestController
@Tag(name = "6. 카테고리", description = "카테고리 관련 API")
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<CategoryResponseDto>> getCategory() {
		List<CategoryResponseDto> categoryResponsDtos = categoryService.getCategories();
		return ResponseEntity.ok().body(categoryResponsDtos);
	}
}
