package taco.klkl.domain.category.service.subcategory;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import taco.klkl.domain.category.dao.subcategory.SubcategoryRepository;
import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SubcategoryServiceImplTest {

	@InjectMocks
	private SubcategoryServiceImpl subcategoryService;

	@Mock
	private SubcategoryRepository subcategoryRepository;

	private final Category category = Category.of(CategoryType.FOOD);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryType.SNACK);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryType.BEVERAGE);
	private final Subcategory subcategory3 = Subcategory.of(category, SubcategoryType.DRINKS);

	@Test
	@DisplayName("부분 이름으로 Subcategory 조회")
	void testFindAllSubcategoriesByPartialString() {
		// given
		String partialName = "foo";
		List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2, subcategory3);
		SubcategorySimpleResponse subcategory1Response = SubcategorySimpleResponse.from(subcategory1);
		SubcategorySimpleResponse subcategory2Response = SubcategorySimpleResponse.from(subcategory2);
		SubcategorySimpleResponse subcategory3Response = SubcategorySimpleResponse.from(subcategory3);

		when(subcategoryRepository.findAllByNameContaining(partialName)).thenReturn(subcategories);

		// when
		List<SubcategorySimpleResponse> subcategorySimpleResponses = subcategoryService
			.findAllSubcategoriesByPartialString(partialName);

		// then
		Assertions.assertThat(subcategorySimpleResponses.size()).isEqualTo(3);
		Assertions.assertThat(subcategorySimpleResponses)
			.containsExactly(subcategory1Response, subcategory2Response, subcategory3Response);
	}
}
