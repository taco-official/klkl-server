package taco.klkl.domain.search.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.search.dto.response.SearchResponse;

@Service
public interface SearchService {
	SearchResponse findSearchResult(final String queryParam);
}
