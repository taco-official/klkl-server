package taco.klkl.domain.notification.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.member.domain.Member;
import taco.klkl.domain.notification.dao.NotificationRepository;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.dto.response.NotificationUpdateResponse;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.Rating;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.region.Region;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Mock
	Region region;

	@Mock
	Currency currency;

	@Mock
	private NotificationRepository notificationRepository;

	@MockBean
	NotificationService notificationService;

	private final Member member = Member.ofUser("name", "0000", null, null);
	private final Country country = Country.of(CountryType.MALAYSIA, region, "wallpaper", currency);
	private final City city = City.of(CityType.BORACAY, country);
	private final Category category = Category.of(CategoryType.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryType.MAKEUP);

	private final Product product = Product.of(
		"name",
		"description",
		"address",
		1000,
		Rating.FIVE,
		member,
		city,
		subcategory,
		currency
	);

	private final Comment comment1 = Comment.of(product, member, "content1");
	private final Comment comment2 = Comment.of(product, member, "content2");
	private Notification notification1;
	private Notification notification2;

	@BeforeEach
	void setUp() {
		notification1 = Notification.of(comment1);
		notification2 = Notification.of(comment2);
	}

	@Test
	@DisplayName("모든 알림 조회")
	void testFindAllNotifications() throws Exception {

		// given
		NotificationResponse notificationResponse = NotificationResponse.from(notification1);
		NotificationResponse notificationResponse2 = NotificationResponse.from(notification2);
		List<NotificationResponse> notificationResponses = List.of(notificationResponse, notificationResponse2);
		when(notificationService.findAllNotifications()).thenReturn(notificationResponses);

		// when & then
		mockMvc.perform(get("/v1/notifications"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data[0].isRead", is(false)))
			.andExpect(jsonPath("$.data[0].product.name", is(product.getName())))
			.andExpect(jsonPath("$.data[0].comment.content", is(comment1.getContent())))
			.andExpect(jsonPath("$.data[1].isRead", is(false)))
			.andExpect(jsonPath("$.data[1].product.name", is(product.getName())))
			.andExpect(jsonPath("$.data[1].comment.content", is(comment2.getContent())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("모든 알림 읽음 테스트")
	void testReadAllNotifications() throws Exception {
		// given
		notification1.read();
		notification2.read();
		NotificationUpdateResponse notificationUpdateResponse = NotificationUpdateResponse.of(2L);
		when(notificationService.readAllNotifications()).thenReturn(notificationUpdateResponse);
		when(notificationRepository.count()).thenReturn(2L);

		// when
		mockMvc.perform(put("/v1/notifications/read"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.updatedCount", is(2)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));

		// then
	}

	@Test
	@DisplayName("단일 알림 읽음 테스트")
	void testReadOneNotifications() throws Exception {
		// given
		notification1.read();
		NotificationUpdateResponse notificationUpdateResponse = NotificationUpdateResponse.of(1L);
		when(notificationService.readNotificationById(1L)).thenReturn(notificationUpdateResponse);

		// when & then
		mockMvc.perform(put("/v1/notifications/1/read"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.updatedCount", is(1)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
