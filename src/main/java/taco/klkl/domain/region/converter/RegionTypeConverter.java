package taco.klkl.domain.region.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.enums.RegionType;

@Converter(autoApply = true)
public class RegionTypeConverter implements AttributeConverter<RegionType, String> {

	@Override
	public String convertToDatabaseColumn(final RegionType regionType) {

		if (regionType == null) {
			return null;
		}

		return regionType.getKoreanName();
	}

	@Override
	public RegionType convertToEntityAttribute(final String dbData) {

		final RegionType regionType = RegionType.getRegionByName(dbData);

		if (regionType.equals(RegionType.NONE)) {
			throw new IllegalArgumentException("Unknown value: " + dbData);
		}

		return regionType;
	}
}
