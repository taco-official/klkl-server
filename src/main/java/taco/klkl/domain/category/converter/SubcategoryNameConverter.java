package taco.klkl.domain.category.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.SubcategoryName;

@Converter(autoApply = true)
public class SubcategoryNameConverter implements AttributeConverter<SubcategoryName, String> {

	@Override
	public String convertToDatabaseColumn(SubcategoryName subcategoryName) {
		if (subcategoryName == null) {
			return null;
		}
		return subcategoryName.getKoreanName();
	}

	@Override
	public SubcategoryName convertToEntityAttribute(String name) {
		if (name == null) {
			return null;
		}
		return SubcategoryName.getByName(name);
	}
}
