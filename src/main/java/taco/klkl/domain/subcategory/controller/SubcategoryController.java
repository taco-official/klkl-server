package taco.klkl.domain.subcategory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.subcategory.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.subcategory.sevice.SubcategoryService;

@Slf4j
@Tag(name = "7. 서브카테고리", description = "서브카테고리 관련 API")
@RestController
@RequestMapping("/v1/categories/{id}/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {
	private final SubcategoryService subcategoryService;

	@GetMapping
	public ResponseEntity<List<SubcategoryResponseDto>> getSubcategory(@PathVariable Long id) {
		List<SubcategoryResponseDto> subcategories = subcategoryService.getSubcategories(id);
		return ResponseEntity.ok(subcategories);
	}
}
