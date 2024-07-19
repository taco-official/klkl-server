package taco.klkl.global.common.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.global.common.enums.Gender;

@Converter(autoApply = true)
public class GenderConvert implements AttributeConverter<Gender, String> {

	@Override
	public String convertToDatabaseColumn(Gender gender) {
		if (gender == null) {
			return null;
		}

		return gender.getDescription();
	}

	@Override
	public Gender convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			throw new IllegalArgumentException("Unknown value" + dbData);
		}

		return switch (dbData) {
			case "남" -> Gender.MALE;
			case "여" -> Gender.FEMALE;
			default -> throw new IllegalArgumentException("Unknown value" + dbData);
		};
	}
}
