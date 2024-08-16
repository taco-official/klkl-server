package taco.klkl.domain.region.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dto.response.CurrencyResponse;
import taco.klkl.domain.region.service.CurrencyService;

@Slf4j
@RestController
@RequestMapping("/v1/currencies")
@RequiredArgsConstructor
@Tag(name = "5. 지역", description = "지역 관련 API")
public class CurrencyController {

	private final CurrencyService currencyService;

	@Operation(summary = "모든 통화 조회", description = "모든 통화 목록을 조회합니다.")
	@GetMapping
	public List<CurrencyResponse> findAllCurrencies() {
		return currencyService.findAllCurrencies();
	}
}
