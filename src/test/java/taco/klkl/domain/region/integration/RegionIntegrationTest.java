package taco.klkl.domain.region.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.service.RegionService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegionIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	RegionService regionService;

	@Test
	@DisplayName("모든 지역 조회 통합 테스트")
	void getAllRegionsTest() throws Exception {
		// given
		List<RegionResponseDto> regionResponseDtos = regionService.getAllRegions();

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(regionResponseDtos.size())));
	}
}
