package taco.klkl.domain.member.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import taco.klkl.domain.member.domain.Follow;
import taco.klkl.domain.member.domain.Member;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	List<Follow> findByFollowerId(final Long followerId);

	boolean existsByFollowerAndFollowing(final Member follower, final Member following);

	void deleteByFollowerAndFollowing(final Member follower, final Member following);
}
