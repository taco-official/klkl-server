package taco.klkl.domain.category.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.FilterName;

@Converter(autoApply = true)
public class FilterNameConverter implements AttributeConverter<FilterName, String> {

	@Override
	public String convertToDatabaseColumn(FilterName filterName) {
		return filterName.getKoreanName();
	}

	@Override
	public FilterName convertToEntityAttribute(String name) {
		return FilterName.getByName(name);
	}
}
