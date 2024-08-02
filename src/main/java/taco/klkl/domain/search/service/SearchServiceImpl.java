package taco.klkl.domain.search.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.dto.response.CountrySimpleResponseDto;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CountryService;
import taco.klkl.domain.search.dto.response.SearchResponseDto;

@Slf4j
@Primary
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

	private final CountryService countryService;
	private final CityService cityService;
	private final ProductService productService;

	@Override
	public SearchResponseDto getSearchResult(String queryParam) {
		List<CountrySimpleResponseDto> findCountries = getCountriesByQueryParam(queryParam);

		return SearchResponseDto.of(findCountries, Collections.emptyList(), Collections.emptyList());
	}

	private List<CountrySimpleResponseDto> getCountriesByQueryParam(String queryParam) {
		List<CountryType> countryTypes = CountryType.getCountriesByPartialString(queryParam);

		return countryTypes.stream()
			.map(countryService::getSimpleCountryByCountryType)
			.toList();
	}
}
