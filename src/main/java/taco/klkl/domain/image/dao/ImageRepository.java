package taco.klkl.domain.image.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.image.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
