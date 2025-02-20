package taco.klkl.domain.region.controller.region;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.region.RegionResponse;
import taco.klkl.domain.region.service.region.RegionService;

@Slf4j
@RestController
@RequestMapping("/v1/regions")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class RegionController {

	private final RegionService regionService;

	@GetMapping("/hierarchy")
	@Operation(summary = "전체 지역의 계층 정보 조회", description = "전체 지역의 계층 정보를 조회합니다.")
	public List<RegionResponse> getAllRegions() {
		return regionService.findAllRegions();
	}
}
