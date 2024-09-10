package taco.klkl.domain.like.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.like.dto.response.LikeResponse;
import taco.klkl.domain.like.service.LikeService;

@Slf4j
@RestController
@RequestMapping("/v1/products/{productId}/likes")
@RequiredArgsConstructor
@Tag(name = "3. 좋아요", description = "좋아요 관련 API")
public class LikeController {

	private final LikeService likeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "좋아요 누르기", description = "상품에 좋아요를 누릅니다.")
	public LikeResponse addLike(@PathVariable final Long productId) {
		return likeService.createLike(productId);
	}

	@DeleteMapping
	@Operation(summary = "좋아요 취소", description = "상품에 누른 좋아요를 취소합니다.")
	public LikeResponse removeLike(@PathVariable final Long productId) {
		return likeService.deleteLike(productId);
	}
}
