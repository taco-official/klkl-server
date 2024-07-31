package taco.klkl.domain.region.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.dto.response.RegionSimpleResponseDto;
import taco.klkl.domain.region.service.RegionService;

@Slf4j
@RestController
@RequestMapping("/v1/regions")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class RegionController {

	private final RegionService regionService;

	@Operation(summary = "모든 지역 조회", description = "모든 지역을 조회합니다.")
	@GetMapping()
	public ResponseEntity<List<RegionSimpleResponseDto>> getAllRegions() {

		List<RegionSimpleResponseDto> findRegions = regionService.getAllRegions();

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(findRegions);
	}

	@Operation(summary = "지역 하나 조회", description = "regionId로 특정 지역을 조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<RegionSimpleResponseDto> getRegionById(@PathVariable Long id) {

		RegionSimpleResponseDto findRegion = regionService.getRegionById(id);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(findRegion);
	}

	@GetMapping("/{id}/countries")
	public ResponseEntity<RegionResponseDto> getCountriesByRegion(@PathVariable Long id) {

		RegionResponseDto findRegion = regionService.getRegionsWithCountries(id);

		return ResponseEntity.ok()
			.contentType(MediaType.APPLICATION_JSON)
			.body(findRegion);
	}
}
