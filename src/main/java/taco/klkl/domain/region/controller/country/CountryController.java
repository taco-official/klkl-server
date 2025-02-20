package taco.klkl.domain.region.controller.country;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.service.country.CountryService;

@Slf4j
@RestController
@RequestMapping("/v1/countries")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class CountryController {

	private final CountryService countryService;

	@GetMapping("/{countryId}")
	@Operation(summary = "단일 국가 정보 조회", description = "countryId로 특정 국가를 조회합니다.")
	public CountryResponse getCountry(@PathVariable final Long countryId) {
		return countryService.getCountryById(countryId);
	}
}
