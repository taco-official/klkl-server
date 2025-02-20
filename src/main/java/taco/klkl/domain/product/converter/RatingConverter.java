package taco.klkl.domain.product.converter;

import java.math.BigDecimal;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.product.domain.Rating;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, BigDecimal> {

	@Override
	public BigDecimal convertToDatabaseColumn(final Rating attribute) {
		if (attribute == null) {
			return null;
		}
		return BigDecimal.valueOf(attribute.getValue());
	}

	@Override
	public Rating convertToEntityAttribute(final BigDecimal dbData) {
		if (dbData == null) {
			return null;
		}
		return Rating.from(dbData.doubleValue());
	}
}
