package taco.klkl.domain.region.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.enums.CountryType;

@Converter(autoApply = true)
public class CountryTypeConvert implements AttributeConverter<CountryType, String> {

	@Override
	public String convertToDatabaseColumn(CountryType countryType) {

		if (countryType == null) {
			return null;
		}

		return countryType.getName();
	}

	@Override
	public CountryType convertToEntityAttribute(String dbData) {
		
		CountryType countryType = CountryType.getCountryTypeByName(dbData);

		if (countryType.equals(CountryType.NONE)) {
			throw new IllegalArgumentException("Unknown values: " + dbData);
		}

		return countryType;
	}
}
