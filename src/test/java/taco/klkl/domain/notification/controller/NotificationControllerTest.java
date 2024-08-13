package taco.klkl.domain.notification.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.comment.domain.Comment;
import taco.klkl.domain.notification.domain.Notification;
import taco.klkl.domain.notification.dto.response.NotificationResponse;
import taco.klkl.domain.notification.service.NotificationService;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.domain.Region;
import taco.klkl.domain.region.enums.CityType;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.UserConstants;
import taco.klkl.global.common.response.PagedResponseDto;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Mock
	Region region;

	@Mock
	Currency currency;

	@MockBean
	NotificationService notificationService;

	private final User user = UserConstants.TEST_USER;
	private final Country country = Country.of(CountryType.MALAYSIA, region, "flag", "photo", currency);
	private final City city = City.of(country, CityType.BORACAY);
	private final Category category = Category.of(CategoryName.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryName.MAKEUP);

	private final Product product = Product.of(
		"name",
		"description",
		"address",
		1000,
		user,
		city,
		subcategory,
		currency
	);

	private final Comment comment1 = Comment.of(product, user, "content1");
	private final Comment comment2 = Comment.of(product, user, "content2");
	private final Notification notification1 = Notification.of(comment1);
	private final Notification notification2 = Notification.of(comment2);

	@BeforeEach
	void setUp() {
		notification1.prePersist();
		notification2.prePersist();
	}

	@Test
	@DisplayName("모든 알림 조회")
	void testGetAllNotifications() throws Exception {

		// given
		NotificationResponse notificationResponse = NotificationResponse.from(notification1);
		NotificationResponse notificationResponse2 = NotificationResponse.from(notification2);
		List<NotificationResponse> notificationResponses = List.of(notificationResponse, notificationResponse2);
		PagedResponseDto<NotificationResponse> pagedResponse = new PagedResponseDto<>(
			notificationResponses, 0, 10, 2, 1, true
		);
		when(notificationService.getNotifications(any(Pageable.class))).thenReturn(pagedResponse);

		// when & then
		mockMvc.perform(get("/v1/notifications")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data.content[0].notification.isRead", is(false)))
			.andExpect(jsonPath("$.data.content[0].product.name", is(product.getName())))
			.andExpect(jsonPath("$.data.content[0].comment.content", is(comment1.getContent())))
			.andExpect(jsonPath("$.data.content[1].notification.isRead", is(false)))
			.andExpect(jsonPath("$.data.content[1].product.name", is(product.getName())))
			.andExpect(jsonPath("$.data.content[1].comment.content", is(comment2.getContent())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(10)))
			.andExpect(jsonPath("$.data.totalElements", is(2)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
