package taco.klkl.domain.category.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.SubcategoryName;

@Converter(autoApply = true)
public class SubcategoryNameConverter implements AttributeConverter<SubcategoryName, String> {

	@Override
	public String convertToDatabaseColumn(SubcategoryName subcategoryName) {
		return subcategoryName.getName();
	}

	@Override
	public SubcategoryName convertToEntityAttribute(String name) {
		return SubcategoryName.getByName(name);
	}
}
