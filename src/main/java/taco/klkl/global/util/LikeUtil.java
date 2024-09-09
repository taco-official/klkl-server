package taco.klkl.global.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.domain.Like;

@Component
@RequiredArgsConstructor
public class LikeUtil {

	private final LikeRepository likeRepository;

	public Page<Like> findLikesByUserId(final Long userId, final Pageable pageable) {
		return likeRepository.findByUserId(userId, pageable);
	}
}
