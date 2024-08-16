package taco.klkl.domain.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.search.dto.response.SearchResponse;
import taco.klkl.domain.search.service.SearchService;

@Slf4j
@RestController
@RequestMapping("/v1/search")
@RequiredArgsConstructor
@Tag(name = "6. 검색", description = "검색 관련 API")
public class SearchController {

	private final SearchService searchService;

	@GetMapping()
	public SearchResponse getSearchByQuery(
		@RequestParam(value = "q")
		@NotBlank
		String query
	) {

		return searchService.getSearchResult(query);
	}
}
