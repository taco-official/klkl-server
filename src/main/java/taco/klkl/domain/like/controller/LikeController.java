package taco.klkl.domain.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.service.LikeService;

@Slf4j
@RestController
@RequestMapping("/v1/products/{id}/likes")
@RequiredArgsConstructor
@Tag(name = "3. 좋아요", description = "좋아요 관련 API")
public class LikeController {

	private final LikeService likeService;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addLike(@PathVariable(value = "id") final Long productId) {
		likeService.createLike(productId);
	}

	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void removeLike(@PathVariable(value = "id") final Long productId) {
		likeService.deleteLike(productId);
	}
}
