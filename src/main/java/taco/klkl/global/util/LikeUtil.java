package taco.klkl.global.util;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.domain.Like;

@Component
@RequiredArgsConstructor
public class LikeUtil {

	private final LikeRepository likeRepository;

	public List<Like> findLikesByUserId(final Long userId) {
		return likeRepository.findAllByUserId(userId);
	}
}
