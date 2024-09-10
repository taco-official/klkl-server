package taco.klkl.domain.region.controller.city;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.city.CityHierarchyResponse;
import taco.klkl.domain.region.service.city.CityService;

@Slf4j
@RestController
@RequestMapping("/v1/cities")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class CityController {

	private final CityService cityService;

	@GetMapping("/{cityId}/hierarchy")
	@Operation(summary = "특정 도시의 계층 정보 조회", description = "특정 도시의 계층 정보를 조회합니다.")
	public CityHierarchyResponse getCityHierarchy(@PathVariable final Long cityId) {
		return cityService.findCityHierarchyById(cityId);
	}
}
