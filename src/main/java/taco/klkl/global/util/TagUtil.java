package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.dao.TagRepository;
import taco.klkl.domain.category.domain.Tag;
import taco.klkl.domain.category.exception.TagNotFoundException;

@Component
@RequiredArgsConstructor
public class TagUtil {

	private final TagRepository tagRepository;

	public Tag findTagEntityById(final Long id) {
		return tagRepository.findById(id)
			.orElseThrow(TagNotFoundException::new);
	}
}
