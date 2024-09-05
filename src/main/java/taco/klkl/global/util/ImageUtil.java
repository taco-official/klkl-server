package taco.klkl.global.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.image.dao.ImageRepository;
import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.image.exception.ImageNotFoundException;

@Component
@RequiredArgsConstructor
public class ImageUtil {

	private final ImageRepository imageRepository;

	public Image findImageEntityById(final Long id) {
		return imageRepository.findById(id)
			.orElseThrow(ImageNotFoundException::new);
	}
}
