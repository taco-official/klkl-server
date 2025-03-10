package taco.klkl.domain.category.controller.category;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.category.dto.response.category.CategoryDetailResponse;
import taco.klkl.domain.category.service.category.CategoryService;

@Slf4j
@RestController
@Tag(name = "6. 카테고리", description = "카테고리 관련 API")
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/hierarchy")
	@Operation(summary = "전체 분류의 계층 정보 조회", description = "전체 분류의 계층 정보를 조회합니다.")
	public List<CategoryDetailResponse> getAllCategories() {
		return categoryService.findAllCategories();
	}
}
