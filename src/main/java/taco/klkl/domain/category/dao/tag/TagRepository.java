package taco.klkl.domain.category.dao.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.category.domain.tag.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
