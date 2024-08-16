package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.Tag;
import taco.klkl.domain.category.domain.TagName;
import taco.klkl.domain.category.dto.response.TagWithSubcategoryResponse;

@ExtendWith(MockitoExtension.class)
public class SubcategoryTagServiceImplTest {

	@InjectMocks
	private SubcategoryTagServiceImpl subcategoryTagService;

	@Mock
	private Subcategory subcategory1;

	@Mock
	private Tag tag1;

	@Mock
	private Tag tag2;

	@Mock
	private SubcategoryTag subcategoryTag1;

	@Mock
	private SubcategoryTag subcategoryTag2;

	@BeforeEach
	public void setUp() {
		List<SubcategoryTag> subcategoryTags = Arrays.asList(subcategoryTag1, subcategoryTag2);

		when(subcategory1.getName()).thenReturn(SubcategoryName.INSTANT_FOOD);
		when(subcategory1.getSubcategoryTags()).thenReturn(subcategoryTags);
		when(subcategoryTag1.getTag()).thenReturn(tag1);
		when(subcategoryTag2.getTag()).thenReturn(tag2);
		when(tag1.getName()).thenReturn(TagName.CONVENIENCE_STORE);
		when(tag2.getName()).thenReturn(TagName.CILANTRO);
	}

	@Test
	@DisplayName("해당 서비스가 SubcategoryWithFilterResponseDto를 반환하는지 테스트")
	public void testGetTagsBySubcategoryList() {
		// given
		final List<Subcategory> subcategoryList = Arrays.asList(subcategory1);

		// when
		final List<TagWithSubcategoryResponse> result = subcategoryTagService.getTagsBySubcategoryList(subcategoryList);

		// then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(SubcategoryName.INSTANT_FOOD.getKoreanName(), result.get(0).name());
		assertEquals(2, result.get(0).tags().size());
		assertEquals(TagName.CONVENIENCE_STORE.getKoreanName(), result.get(0).tags().get(0).name());
		assertEquals(TagName.CILANTRO.getKoreanName(), result.get(0).tags().get(1).name());
	}
}
