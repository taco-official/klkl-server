package taco.klkl.domain.region.controller.country;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.city.CityResponse;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.service.country.CountryService;

@Slf4j
@RestController
@RequestMapping("/v1/countries")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class CountryController {

	private final CountryService countryService;

	@Operation(summary = "모든 국가 조회", description = "모든 국가를 조회합니다.")
	@GetMapping()
	public List<CountryResponse> findAllCountries() {
		return countryService.findAllCountries();
	}

	@Operation(summary = "국가 하나 조회", description = "countryId로 특정 국가를 조회합니다.")
	@GetMapping("/{countryId}")
	public CountryResponse findCountryById(@PathVariable final Long countryId) {
		return countryService.findCountryById(countryId);
	}

	@Operation(summary = "국가에 속한 모든 도시 조회", description = "countryId로 특정 국가에 속한 도시들을 조회합니다.")
	@GetMapping("/{countryId}/cities")
	public List<CityResponse> findCitiesByCountryId(@PathVariable final Long countryId) {
		return countryService.findCitiesByCountryId(countryId);
	}
}
