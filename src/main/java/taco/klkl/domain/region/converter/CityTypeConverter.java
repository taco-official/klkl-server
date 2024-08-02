package taco.klkl.domain.region.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.enums.CityType;

@Converter(autoApply = true)
public class CityTypeConverter implements AttributeConverter<CityType, String> {

	@Override
	public String convertToDatabaseColumn(CityType cityType) {

		if (cityType == null) {
			return null;
		}

		return cityType.getKoreanName();
	}

	@Override
	public CityType convertToEntityAttribute(final String dbData) {

		final CityType cityType = CityType.getCityTypeByKoreanName(dbData);

		if (cityType.equals(CityType.NONE)) {
			throw new IllegalArgumentException("Unknown value: " + dbData);
		}

		return CityType.getCityTypeByKoreanName(dbData);
	}
}
