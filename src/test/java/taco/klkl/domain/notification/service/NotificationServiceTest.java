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
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.domain.QNotification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.dto.response.NotificationUpdateResponse;
import taco.klkl.domain.notification.exception.NotificationNotFoundException;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.domain.region.RegionType;
import taco.klkl.domain.user.domain.QUser;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@ExtendWith(MockitoExtension.class)
@Transactional
public class NotificationServiceTest {

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private UserUtil userUtil;

	@InjectMocks
	private NotificationServiceImpl notificationService;

	@Mock
	private User mockUser;

	@Mock
	private Notification mockNotification;

	private Product product;
	private User commentUser;
	private Comment comment;

	@BeforeEach
	public void setUp() {
		commentUser = User.of(
			"윤상정",
			"나는 해적왕이 될 사나이다.");

		Region region = Region.from(RegionType.SOUTHEAST_ASIA);
		Currency currency = Currency.of(
			CurrencyType.THAI_BAHT
		);
		Country country = Country.of(
			CountryType.JAPAN,
			region,
			"image/thailand-photo.jpg",
			currency
		);
		City city = City.of(
			CityType.BANGKOK,
			country
		);

		Category category = Category.of(CategoryType.FOOD);
		Subcategory subcategory = Subcategory.of(
			category,
			SubcategoryType.INSTANT_FOOD
		);

		product = Product.of(
			"name",
			"description",
			"address",
			1000,
			Rating.FIVE,
			mockUser,
			city,
			subcategory,
			currency
		);
		comment = Comment.of(product, commentUser, "윤상정 바보");
	}

	@Test
	@DisplayName("댓글 알림이 비어있지 않을경우 조회 성공")
	public void testFindAllNotifications() {
		//given
		JPAQuery<Notification> mockQuery = mock(JPAQuery.class);
		List<Notification> notificationList = List.of(mockNotification);
		QNotification notification = QNotification.notification;
		QUser user = QUser.user;

		when(userUtil.getCurrentUser()).thenReturn(mockUser);
		when(mockNotification.getId()).thenReturn(1L);
		when(mockNotification.getIsRead()).thenReturn(false);
		when(mockNotification.getCreatedAt()).thenReturn(LocalDateTime.now());
		when(mockNotification.getComment()).thenReturn(comment);

		when(queryFactory.selectFrom(any(QNotification.class))).thenReturn(mockQuery);
		when(mockQuery.join(notification.comment.product.user, user)).thenReturn(mockQuery);
		when(mockQuery.where(any(BooleanExpression.class))).thenReturn(mockQuery);
		when(mockQuery.orderBy(any(OrderSpecifier.class), any(OrderSpecifier.class))).thenReturn(mockQuery);
		when(mockQuery.fetch()).thenReturn(notificationList);

		//when
		List<NotificationResponse> response = notificationService.findAllNotifications();

		//then
		assertThat(response).hasSize(1);
		assertThat(response.get(0).id()).isEqualTo(mockNotification.getId());
		assertThat(response.get(0).isRead()).isFalse();
	}

	@Test
	@DisplayName("댓글 알림이 빈경우 조회 성공")
	public void testGetBlankNotifications() {
		//given
		JPAQuery<Notification> mockQuery = mock(JPAQuery.class);
		List<Notification> notificationList = Collections.emptyList();
		QNotification notification = QNotification.notification;
		QUser user = QUser.user;

		when(userUtil.getCurrentUser()).thenReturn(mockUser);

		when(queryFactory.selectFrom(any(QNotification.class))).thenReturn(mockQuery);
		when(mockQuery.join(notification.comment.product.user, user)).thenReturn(mockQuery);
		when(mockQuery.where(any(BooleanExpression.class))).thenReturn(mockQuery);
		when(mockQuery.orderBy(any(OrderSpecifier.class), any(OrderSpecifier.class))).thenReturn(mockQuery);
		when(mockQuery.fetch()).thenReturn(notificationList);

		//when
		List<NotificationResponse> response = notificationService.findAllNotifications();

		//then
		assertThat(response).hasSize(0);
	}

	@Test
	@DisplayName("모든 댓글 알림 읽기 성공")
	public void testReadAllNotifications() {
		//given
		Notification notification1 = Notification.of(comment);
		Notification notification2 = Notification.of(comment);

		List<Notification> notificationList = List.of(notification1, notification2);

		when(userUtil.getCurrentUser()).thenReturn(mockUser);
		when(notificationRepository.findByComment_Product_User(mockUser)).thenReturn(notificationList);
		when(notificationRepository.count()).thenReturn(2L);

		//when
		NotificationUpdateResponse response = notificationService.readAllNotifications();

		//then
		assertThat(response.updatedCount()).isEqualTo(2L);
		verify(notificationRepository).findByComment_Product_User(mockUser);
	}

	@Test
	@DisplayName("댓글 알림 단일 읽기 성공")
	public void testReadNotificationsById() {
		//given
		Notification notification = Notification.of(comment);

		when(notificationRepository.findById(any(Long.class))).thenReturn(Optional.of(notification));

		//when
		NotificationUpdateResponse response = notificationService.readNotificationById(any(Long.class));

		//then
		assertThat(response.updatedCount()).isEqualTo(1L);
		verify(notificationRepository).findById(any(Long.class));
	}

	@Test
	@DisplayName("댓글 알림 단일 읽기 실패")
	public void testReadNotificationsByIdWithWrongId() {
		//given
		when(notificationRepository.findById(any(Long.class))).thenThrow(NotificationNotFoundException.class);

		//when & then
		assertThrows(NotificationNotFoundException.class,
			() -> notificationService.readNotificationById(any(Long.class)));
		verify(notificationRepository).findById(any(Long.class));
	}
}
