package taco.klkl.domain.image.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.image.domain.Image;
import taco.klkl.domain.image.domain.ImageType;

public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByImageTypeAndTargetId(final ImageType imageType, final Long targetId);

	Optional<Image> findByImageTypeAndTargetIdAndImageKey(
		final ImageType imageType, final Long targetId, final String imageKey);
}
