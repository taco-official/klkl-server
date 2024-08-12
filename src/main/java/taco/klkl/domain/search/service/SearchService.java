package taco.klkl.domain.search.service;

import org.springframework.stereotype.Service;

import taco.klkl.domain.search.dto.response.SearchResponseDto;

@Service
public interface SearchService {
	SearchResponseDto getSearchResult(String queryParam);
}
