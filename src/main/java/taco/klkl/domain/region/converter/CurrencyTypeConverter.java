package taco.klkl.domain.region.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.region.enums.CurrencyType;

@Converter(autoApply = true)
public class CurrencyTypeConverter implements AttributeConverter<CurrencyType, String> {
	@Override
	public String convertToDatabaseColumn(CurrencyType currencyType) {

		if (currencyType == null) {
			return null;
		}

		return currencyType.getCodeName();
	}

	@Override
	public CurrencyType convertToEntityAttribute(String dbData) {

		final CurrencyType findCurrency = CurrencyType.getCurrencyTypeByCode(dbData);

		if (findCurrency.equals(CurrencyType.NONE)) {
			throw new IllegalArgumentException("Unknown value: " + dbData);
		}

		return findCurrency;
	}
}
