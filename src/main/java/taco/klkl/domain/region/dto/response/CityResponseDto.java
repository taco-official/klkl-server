package taco.klkl.domain.region.dto.response;

import java.util.Objects;

import taco.klkl.domain.region.domain.City;

public record CityResponseDto(
	Long cityId,
	String name
) {
	public static CityResponseDto from(City city) {
		return new CityResponseDto(
			city.getCityId(),
			city.getName().getKoreanName()
		);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		CityResponseDto that = (CityResponseDto)object;
		return Objects.equals(cityId, that.cityId)
			&& Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cityId, name);
	}

	@Override
	public String toString() {
		return "CityResponseDto{"
			+ "cityId=" + cityId
			+ ", name='" + name + '\''
			+ '}';
	}
}
