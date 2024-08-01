package taco.klkl.domain.region.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.enums.CountryType;

@Converter(autoApply = true)
public class CountryTypeConverter implements AttributeConverter<CountryType, String> {

	@Override
	public String convertToDatabaseColumn(final CountryType countryType) {

		if (countryType == null) {
			return null;
		}

		return countryType.getKoreanName();
	}

	@Override
	public CountryType convertToEntityAttribute(final String dbData) {

		final CountryType countryType = CountryType.getCountryTypeByName(dbData);

		if (countryType.equals(CountryType.NONE)) {
			throw new IllegalArgumentException("Unknown values: " + dbData);
		}

		return countryType;
	}
}
