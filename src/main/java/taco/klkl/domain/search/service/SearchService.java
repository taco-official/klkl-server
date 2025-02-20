package taco.klkl.domain.search.service;

import taco.klkl.domain.search.dto.response.SearchResponse;

public interface SearchService {
	SearchResponse findSearchResult(final String queryParam);
}
