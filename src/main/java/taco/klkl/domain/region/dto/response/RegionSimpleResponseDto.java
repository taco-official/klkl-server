package taco.klkl.domain.region.dto.response;

import java.util.Objects;

import taco.klkl.domain.region.domain.Region;

public record RegionSimpleResponseDto(
	Long regionId,
	String name
) {

	public static RegionSimpleResponseDto from(Region region) {
		return new RegionSimpleResponseDto(region.getRegionId(), region.getName().getName());
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		RegionSimpleResponseDto that = (RegionSimpleResponseDto)object;
		return Objects.equals(name, that.name) && Objects.equals(regionId, that.regionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(regionId, name);
	}
}
