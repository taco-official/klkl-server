package taco.klkl.domain.region.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.domain.CityType;

@Converter(autoApply = true)
public class CityTypeConverter implements AttributeConverter<CityType, String> {

	@Override
	public String convertToDatabaseColumn(final CityType cityType) {
		if (cityType == null) {
			return null;
		}
		return cityType.getKoreanName();
	}

	@Override
	public CityType convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		return CityType.getCityTypeByKoreanName(dbData);
	}
}
