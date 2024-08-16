package taco.klkl.domain.category.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.SubcategoryName;

@Converter(autoApply = true)
public class SubcategoryNameConverter implements AttributeConverter<SubcategoryName, String> {

	@Override
	public String convertToDatabaseColumn(final SubcategoryName subcategoryName) {
		if (subcategoryName == null) {
			return null;
		}
		return subcategoryName.getKoreanName();
	}

	@Override
	public SubcategoryName convertToEntityAttribute(final String name) {
		if (name == null) {
			return null;
		}
		return SubcategoryName.fromKoreanName(name);
	}
}
