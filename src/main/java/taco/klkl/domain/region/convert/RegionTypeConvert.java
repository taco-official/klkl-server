package taco.klkl.domain.region.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.enums.RegionType;

@Converter(autoApply = true)
public class RegionTypeConvert implements AttributeConverter<RegionType, String> {

	@Override
	public String convertToDatabaseColumn(RegionType regionType) {

		if (regionType == null) {
			return null;
		}

		return regionType.getName();
	}

	@Override
	public RegionType convertToEntityAttribute(String dbData) {
		
		RegionType regionType = RegionType.getRegionByName(dbData);

		if (regionType.equals(RegionType.NONE)) {
			throw new IllegalArgumentException("Unknown value: " + dbData);
		}

		return regionType;
	}
}
