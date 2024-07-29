package taco.klkl.domain.region.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.dto.response.RegionResponseDto;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.region.service.RegionService;
import taco.klkl.global.error.exception.ErrorCode;

@WebMvcTest(RegionController.class)
class RegionControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	RegionService regionService;

	@Test
	@DisplayName("모든 지역 조회 성공 테스트")
	void getAllRegionsTest() throws Exception {
		// given
		List<RegionResponseDto> regionResponseDtos = Arrays.asList(
			RegionResponseDto.from(Region.of(RegionType.NORTHEAST_ASIA)),
			RegionResponseDto.from(Region.of(RegionType.SOUTHEAST_ASIA)),
			RegionResponseDto.from(Region.of(RegionType.ETC_REGION))
		);

		when(regionService.getAllRegions()).thenReturn(regionResponseDtos);

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(3)))
			.andExpect(jsonPath("$.data[0].name", is(RegionType.NORTHEAST_ASIA.getName())))
			.andExpect(jsonPath("$.data[1].name", is(RegionType.SOUTHEAST_ASIA.getName())))
			.andExpect(jsonPath("$.data[2].name", is(RegionType.ETC_REGION.getName())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(regionService, times(1)).getAllRegions();
	}

	@Test
	@DisplayName("모든 지역 조회 empty 테스트")
	void getAllRegionsEmptyTest() throws Exception {
		// given
		when(regionService.getAllRegions()).thenReturn(Collections.emptyList());

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", hasSize(0)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(regionService, times(1)).getAllRegions();
	}

	@Test
	@DisplayName("모든 지역 조회 실패 테스트")
	void getRegionsFailTest() throws Exception {
		// given
		when(regionService.getAllRegions()).thenThrow(RuntimeException.class);

		// when & then
		mockMvc.perform(get("/v1/regions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$.isSuccess", is(false)))
			.andExpect(jsonPath("$.code", is(ErrorCode.INTERNAL_SERVER_ERROR.getCode())))
			.andExpect(jsonPath("$.data.message", is(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		verify(regionService, times(1)).getAllRegions();
	}

}
