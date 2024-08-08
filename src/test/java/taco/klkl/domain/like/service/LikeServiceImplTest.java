package taco.klkl.domain.like.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.like.dao.LikeRepository;
import taco.klkl.domain.like.domain.Like;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

class LikeServiceImplTest {

	@Mock
	private LikeRepository likeRepository;

	@Mock
	private ProductService productService;

	@Mock
	private UserUtil userUtil;

	@InjectMocks
	private LikeServiceImpl likeService;

	@Mock
	private Product product;

	@Mock
	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("새로운 좋아요 데이터 생성 테스트")
	void testCreateLike() {
		// given
		Long productId = 1L;

		when(productService.getProductById(productId)).thenReturn(product);
		when(userUtil.getCurrentUser()).thenReturn(user);
		when(likeRepository.existsByProductAndUser(product, user)).thenReturn(false);

		// when
		likeService.createLike(productId);

		// then
		verify(likeRepository).save(any(Like.class));
		verify(productService).increaseLikeCount(product);
	}

	@Test
	@DisplayName("이미 존재하는 좋아요 생성 테스트")
	void testCreateWhenAlreadyExists() {
		// given
		Long productId = 1L;

		when(productService.getProductById(productId)).thenReturn(product);
		when(userUtil.getCurrentUser()).thenReturn(user);
		when(likeRepository.existsByProductAndUser(product, user)).thenReturn(true);

		// when
		likeService.createLike(productId);

		// then
		verify(likeRepository, never()).save(any(Like.class));
		verify(productService, never()).increaseLikeCount(product);
	}

	@Test
	@DisplayName("존재하는 좋아요 데이터 삭제 테스트")
	void testDeleteLike() {
		// given
		Long productId = 1L;

		when(productService.getProductById(productId)).thenReturn(product);
		when(userUtil.getCurrentUser()).thenReturn(user);
		when(likeRepository.existsByProductAndUser(product, user)).thenReturn(true);

		// when
		likeService.deleteLike(productId);

		// then
		verify(likeRepository).deleteByProductAndUser(product, user);
		verify(productService).decreaseLikeCount(product);
	}

	@Test
	@DisplayName("존재하지 않는 좋아요 데이터 삭제 테스트")
	void testDeleteLikeWhenNotExists() {
		// given
		Long productId = 1L;

		when(productService.getProductById(productId)).thenReturn(product);
		when(userUtil.getCurrentUser()).thenReturn(user);
		when(likeRepository.existsByProductAndUser(product, user)).thenReturn(false);

		// when
		likeService.deleteLike(productId);

		// then
		verify(likeRepository, never()).deleteByProductAndUser(product, user);
		verify(productService, never()).decreaseLikeCount(product);
	}
}
