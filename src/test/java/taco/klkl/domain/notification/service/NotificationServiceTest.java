package taco.klkl.domain.notification.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.comment.service.CommentService;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.domain.QNotification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.exception.NotificationNotFoundException;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.region.enums.CurrencyType;
import taco.klkl.domain.region.enums.RegionType;
import taco.klkl.domain.user.domain.Gender;
import taco.klkl.domain.user.domain.User;
import taco.klkl.domain.user.service.UserService;
import taco.klkl.global.common.response.PagedResponseDto;
import taco.klkl.global.util.UserUtil;

@ExtendWith(MockitoExtension.class)
@Transactional
public class NotificationServiceTest {
	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private UserService userService;

	@Mock
	private CommentService commentService;

	@Mock
	private ProductService productService;

	@Mock
	private UserUtil userUtil;

	@InjectMocks
	private NotificationServiceImpl notificationService;

	@Mock
	private User mockUser;

	@Mock
	private Comment mockComment;

	@Mock
	private Notification mockNotification;

	private Product product;
	private User commentUser;
	private Comment comment;

	@BeforeEach
	public void setUp() {
		commentUser = User.of("profile",
			"윤상정",
			Gender.FEMALE,
			26,
			"나는 해적왕이 될 사나이다.");

		Region region = Region.of(RegionType.SOUTHEAST_ASIA);
		Currency currency = Currency.of(
			CurrencyType.THAI_BAHT,
			"image/baht.jpg"
		);
		Country country = Country.of(
			CountryType.JAPAN,
			region,
			"image/thailand-flag.jpg",
			"image/thailand-photo.jpg",
			currency
		);
		City city = City.of(
			country,
			CityType.BANGKOK
		);

		Category category = Category.of(CategoryName.FOOD);
		Subcategory subcategory = Subcategory.of(
			category,
			SubcategoryName.INSTANT_FOOD
		);

		product = Product.of(
			"name",
			"description",
			"address",
			1000,
			mockUser,
			city,
			subcategory,
			currency
		);
		comment = Comment.of(product, commentUser, "윤상정 바보");
	}

	@Test
	@DisplayName("댓글 알림이 비어있지 않을경우 조회 성공")
	public void testGetNotifications() {
		//given
		when(userUtil.findTestUser()).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(1L);
		when(mockNotification.getId()).thenReturn(1L);
		when(mockNotification.getIsRead()).thenReturn(false);
		when(mockNotification.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(mockNotification.getComment()).thenReturn(comment);

		QNotification notification = QNotification.notification;

		JPAQuery<Notification> notificationQuery = mock(JPAQuery.class);
		JPAQuery<Long> countQuery = mock(JPAQuery.class);

		Pageable pageable = PageRequest.of(0, 5);

		List<Notification> notificationList = List.of(mockNotification);

		when(queryFactory.selectFrom(notification)).thenReturn(notificationQuery);
		when(notificationQuery.where(any(BooleanBuilder.class))).thenReturn(notificationQuery);
		when(notificationQuery.offset(pageable.getOffset())).thenReturn(notificationQuery);
		when(notificationQuery.limit(pageable.getPageSize())).thenReturn(notificationQuery);
		when(notificationQuery.fetch()).thenReturn(notificationList);

		when(queryFactory.select(notification.count())).thenReturn(countQuery);
		when(countQuery.from(notification)).thenReturn(countQuery);
		when(countQuery.where(any(BooleanBuilder.class))).thenReturn(countQuery);
		when(countQuery.fetchOne()).thenReturn((long)notificationList.size());

		//when
		PagedResponseDto<NotificationResponse> response = notificationService.getNotifications(pageable);

		//then
		assertThat(response.content()).hasSize(1);
		assertThat(response.content().get(0).notification().notificationId()).isEqualTo(mockNotification.getId());
		assertThat(response.totalElements()).isEqualTo(1);
		assertThat(response.totalPages()).isEqualTo(1);
		assertThat(response.pageNumber()).isEqualTo(0);
		assertThat(response.last()).isTrue();
	}

	@Test
	@DisplayName("댓글 알림이 빈경우 조회 성공")
	public void testGetBlankNotifications() {
		//given
		when(userUtil.findTestUser()).thenReturn(mockUser);
		when(mockUser.getId()).thenReturn(1L);

		QNotification notification = QNotification.notification;

		JPAQuery<Notification> notificationQuery = mock(JPAQuery.class);
		JPAQuery<Long> countQuery = mock(JPAQuery.class);

		Pageable pageable = PageRequest.of(0, 5);

		List<Notification> notificationList = Collections.emptyList();

		when(queryFactory.selectFrom(notification)).thenReturn(notificationQuery);
		when(notificationQuery.where(any(BooleanBuilder.class))).thenReturn(notificationQuery);
		when(notificationQuery.offset(pageable.getOffset())).thenReturn(notificationQuery);
		when(notificationQuery.limit(pageable.getPageSize())).thenReturn(notificationQuery);
		when(notificationQuery.fetch()).thenReturn(notificationList);

		when(queryFactory.select(notification.count())).thenReturn(countQuery);
		when(countQuery.from(notification)).thenReturn(countQuery);
		when(countQuery.where(any(BooleanBuilder.class))).thenReturn(countQuery);
		when(countQuery.fetchOne()).thenReturn((long)notificationList.size());

		//when
		PagedResponseDto<NotificationResponse> response = notificationService.getNotifications(pageable);
		//then
		assertThat(response.content()).hasSize(0);
		assertThat(response.totalElements()).isEqualTo(0);
		assertThat(response.totalPages()).isEqualTo(0);
		assertThat(response.pageNumber()).isEqualTo(0);
		assertThat(response.last()).isTrue();
	}

	@Test
	@DisplayName("모든 댓글 알림 읽기 성공")
	public void testReadAllNotifications() {
		//given
		Notification notification1 = Notification.of(comment);

		List<Notification> notificationList = List.of(notification1, notification1);

		when(userUtil.findTestUser()).thenReturn(mockUser);
		when(notificationRepository.findAllByComment_Product_User(mockUser)).thenReturn(notificationList);

		//when
		List<NotificationResponse> response = notificationService.readAllNotifications();

		//then
		assertThat(response.get(0).notification().isRead()).isEqualTo(true);
		assertThat(response.get(1).notification().isRead()).isEqualTo(true);
		verify(notificationRepository).findAllByComment_Product_User(mockUser);
	}

	@Test
	@DisplayName("댓글 알림 단일 읽기 성공")
	public void testReadNotificationsById() {
		//given
		Notification notification = Notification.of(comment);

		when(notificationRepository.findById(any(Long.class))).thenReturn(Optional.of(notification));

		//when
		NotificationResponse response = notificationService.readNotificationById(any(Long.class));

		//then
		assertThat(response.notification().isRead()).isEqualTo(true);
		verify(notificationRepository).findById(any(Long.class));
	}

	@Test
	@DisplayName("댓글 알림 단일 읽기 실패")
	public void testReadNotificationsByIdWithWrongId() {
		//given
		Notification notification = Notification.of(comment);

		when(notificationRepository.findById(any(Long.class))).thenThrow(NotificationNotFoundException.class);

		//when & then
		assertThrows(NotificationNotFoundException.class,
			() -> notificationService.readNotificationById(any(Long.class)));
		verify(notificationRepository).findById(any(Long.class));
	}
}
