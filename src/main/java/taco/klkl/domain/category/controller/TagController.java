package taco.klkl.domain.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.dto.response.SubcategoryWithTagsResponse;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.category.service.SubcategoryTagService;

@Slf4j
@Tag(name = "7. 서브카테고리", description = "서브카테고리 관련 API")
@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {
	private final SubcategoryService subcategoryService;
	private final SubcategoryTagService subcategoryTagService;

	@GetMapping
	@Operation(description = "Subcategory 포함된 Tag 반환")
	public List<SubcategoryWithTagsResponse> findAllTagsBySubcategoryIds(
		@RequestParam("subcategories") List<Long> subcategoryIds) {
		final List<Subcategory> subcategoryList = subcategoryService.findSubcategoriesBySubcategoryIds(subcategoryIds);
		return subcategoryTagService.findSubcategoryTagsBySubcategoryList(subcategoryList);
	}
}
