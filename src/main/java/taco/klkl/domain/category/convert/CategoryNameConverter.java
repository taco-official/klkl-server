package taco.klkl.domain.category.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.CategoryName;

@Converter(autoApply = true)
public class CategoryNameConverter implements AttributeConverter<CategoryName, String> {

	@Override
	public String convertToDatabaseColumn(CategoryName categoryName) {
		return categoryName.getName();
	}

	@Override
	public CategoryName convertToEntityAttribute(String name) {
		return CategoryName.getByName(name);
	}
}
