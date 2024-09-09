package taco.klkl.domain.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.user.domain.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	List<Follow> findAllByFollowerId(final Long followerId);
}
