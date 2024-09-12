package taco.klkl.domain.category.controller.subcategory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dto.response.subcategory.SubcategoryHierarchyResponse;
import taco.klkl.domain.category.service.subcategory.SubcategoryService;

@Slf4j
@RestController
@Tag(name = "6. 카테고리", description = "카테고리 관련 API")
@RequestMapping("/v1/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {

	private final SubcategoryService subcategoryService;

	@GetMapping("/{subcategoryId}/hierarchy")
	@Operation(summary = "특정 소분류의 계층 정보 조회", description = "특정 소분류의 계층 정보를 조회합니다.")
	public SubcategoryHierarchyResponse getSubcategoryHierarchy(@PathVariable final Long subcategoryId) {
		return subcategoryService.findSubcategoryHierarchyById(subcategoryId);
	}
}
