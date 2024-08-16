package taco.klkl.domain.category.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.CategoryName;

@Converter(autoApply = true)
public class CategoryNameConverter implements AttributeConverter<CategoryName, String> {

	@Override
	public String convertToDatabaseColumn(final CategoryName categoryName) {
		if (categoryName == null) {
			return null;
		}
		return categoryName.getKoreanName();
	}

	@Override
	public CategoryName convertToEntityAttribute(final String name) {
		if (name == null) {
			return null;
		}
		return CategoryName.fromKoreanName(name);
	}
}
