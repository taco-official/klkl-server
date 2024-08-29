package taco.klkl.domain.region.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.domain.CurrencyType;

@Converter(autoApply = true)
public class CurrencyTypeConverter implements AttributeConverter<CurrencyType, String> {
	@Override
	public String convertToDatabaseColumn(final CurrencyType currencyType) {
		if (currencyType == null) {
			return null;
		}
		return currencyType.getCode();
	}

	@Override
	public CurrencyType convertToEntityAttribute(final String dbData) {
		if (dbData == null) {
			return null;
		}
		return CurrencyType.getCurrencyTypeByCodeName(dbData);
	}
}
