package taco.klkl.domain.category.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.dto.response.TagResponse;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.category.service.SubcategoryTagService;

@Slf4j
@Tag(name = "6. 카테고리", description = "카테고리 관련 API")
@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {
	private final SubcategoryService subcategoryService;
	private final SubcategoryTagService subcategoryTagService;

	@GetMapping
	@Operation(summary = "선택된 소분류의 태그 목록 조회", description = "선택된 소분류의 태그 목록을 조회합니다.")
	public Set<TagResponse> findAllTagsBySubcategoryIds(
		@RequestParam(value = "subcategory_id") final List<Long> subcategoryIds
	) {
		final List<Subcategory> subcategoryList = subcategoryService.findSubcategoriesBySubcategoryIds(subcategoryIds);
		return subcategoryTagService.findTagsBySubcategoryList(subcategoryList);
	}
}
