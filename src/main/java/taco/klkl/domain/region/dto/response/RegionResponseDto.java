package taco.klkl.domain.region.dto.response;

import java.util.List;
import java.util.Objects;

import taco.klkl.domain.region.domain.Region;

public record RegionResponseDto(
	Long regionId,
	String name,
	List<CountryWithOutRegionDto> countries
) {

	public static RegionResponseDto from(Region region) {
		return new RegionResponseDto(region.getRegionId(),
			region.getName().getKoreanName(),
			region.getCountries().stream()
				.map(CountryWithOutRegionDto::from).toList());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		RegionResponseDto that = (RegionResponseDto)object;
		return Objects.equals(name, that.name)
			&& Objects.equals(regionId, that.regionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(regionId, name);
	}
}
