package taco.klkl.domain.region.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.CityResponse;
import taco.klkl.domain.region.dto.response.CountryResponse;
import taco.klkl.domain.region.service.CountryService;

@Slf4j
@RestController
@RequestMapping("/v1/countries")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class CountryController {

	private final CountryService countryService;

	@Operation(summary = "모든 국가 조회", description = "모든 국가를 조회합니다.")
	@GetMapping()
	public ResponseEntity<List<CountryResponse>> getAllCountries() {

		final List<CountryResponse> findCountries = countryService.getAllCountries();

		return ResponseEntity.ok().body(findCountries);
	}

	@Operation(summary = "국가 하나 조회", description = "countryId로 특정 국가를 조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<CountryResponse> getCountryById(@PathVariable final Long id) {

		final CountryResponse findCountry = countryService.getCountryById(id);

		return ResponseEntity.ok().body(findCountry);
	}

	@Operation(summary = "국가에 속한 모든 도시 조회", description = "countryId로 특정 국가에 속한 도시들을 조회합니다.")
	@GetMapping("/{id}/cities")
	public ResponseEntity<List<CityResponse>> getCitiesByCountryId(@PathVariable final Long id) {

		final List<CityResponse> findCountry = countryService.getCitiesByCountryId(id);

		return ResponseEntity.ok().body(findCountry);
	}
}
