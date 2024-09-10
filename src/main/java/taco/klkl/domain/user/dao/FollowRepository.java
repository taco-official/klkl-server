package taco.klkl.domain.user.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.user.domain.Follow;
import taco.klkl.domain.user.domain.User;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	List<Follow> findByFollowerId(final Long followerId);

	boolean existsByFollowerAndFollowing(final User follower, final User following);

	void deleteByFollowerAndFollowing(final User follower, final User following);
}
