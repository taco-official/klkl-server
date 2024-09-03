package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.category.dao.SubcategoryTagRepository;
import taco.klkl.domain.category.domain.SubcategoryTag;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.tag.Tag;
import taco.klkl.domain.category.domain.tag.TagType;
import taco.klkl.domain.category.dto.response.tag.TagResponse;

@ExtendWith(MockitoExtension.class)
public class SubcategoryTagServiceImplTest {

	@InjectMocks
	private SubcategoryTagServiceImpl subcategoryTagService;

	@Mock
	private SubcategoryTagRepository subcategoryTagRepository;

	@Mock
	private Subcategory subcategory1;

	@Mock
	private Subcategory subcategory2;

	@Mock
	private Tag tag1;

	@Mock
	private Tag tag2;

	@Mock
	private SubcategoryTag subcategoryTag1;

	@Mock
	private SubcategoryTag subcategoryTag2;

	@Test
	@DisplayName("SubcategoryTagService가 TagResponse 세트를 올바르게 반환하는지 테스트")
	public void testFindTagsBySubcategoryList() {
		// given
		List<Subcategory> subcategoryList = Arrays.asList(subcategory1, subcategory2);

		when(subcategoryTagRepository.findAllBySubcategory(subcategory1))
			.thenReturn(Arrays.asList(subcategoryTag1, subcategoryTag2));
		when(subcategoryTagRepository.findAllBySubcategory(subcategory2))
			.thenReturn(Arrays.asList(subcategoryTag1));

		when(subcategoryTag1.getTag()).thenReturn(tag1);
		when(subcategoryTag2.getTag()).thenReturn(tag2);

		when(tag1.getId()).thenReturn(1L);
		when(tag1.getName()).thenReturn(TagType.CONVENIENCE_STORE.getName());
		when(tag2.getId()).thenReturn(2L);
		when(tag2.getName()).thenReturn(TagType.CILANTRO.getName());

		// when
		Set<TagResponse> result = subcategoryTagService.findTagsBySubcategoryList(subcategoryList);

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertTrue(result.stream().anyMatch(
			tag -> tag.id().equals(1L) && tag.name().equals(TagType.CONVENIENCE_STORE.getName()))
		);
		assertTrue(result.stream().anyMatch(
			tag -> tag.id().equals(2L) && tag.name().equals(TagType.CILANTRO.getName()))
		);

		verify(subcategoryTagRepository, times(1)).findAllBySubcategory(subcategory1);
		verify(subcategoryTagRepository, times(1)).findAllBySubcategory(subcategory2);
	}

	@Test
	@DisplayName("빈 Subcategory 리스트에 대해 빈 TagResponse 세트를 반환하는지 테스트")
	public void testFindTagsByEmptySubcategoryList() {
		// given
		List<Subcategory> emptySubcategoryList = Arrays.asList();

		// when
		Set<TagResponse> result = subcategoryTagService.findTagsBySubcategoryList(emptySubcategoryList);

		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(subcategoryTagRepository, never()).findAllBySubcategory(any());
	}
}
