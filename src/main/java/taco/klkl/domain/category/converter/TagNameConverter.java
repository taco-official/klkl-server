package taco.klkl.domain.category.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import taco.klkl.domain.category.domain.TagName;

@Converter(autoApply = true)
public class TagNameConverter implements AttributeConverter<TagName, String> {

	@Override
	public String convertToDatabaseColumn(final TagName tagName) {
		if (tagName == null) {
			return null;
		}
		return tagName.getKoreanName();
	}

	@Override
	public TagName convertToEntityAttribute(final String name) {
		if (name == null) {
			return null;
		}
		return TagName.fromKoreanName(name);
	}
}
