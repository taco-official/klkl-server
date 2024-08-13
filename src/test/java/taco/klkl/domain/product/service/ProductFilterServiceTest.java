package taco.klkl.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.category.domain.Filter;
import taco.klkl.domain.category.domain.FilterName;
import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.category.service.FilterService;
import taco.klkl.domain.product.dao.ProductFilterRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.domain.ProductFilter;

@DisplayName("ProductFilterService 테스트")
class ProductFilterServiceTest {

	@InjectMocks
	private ProductFilterService productFilterService;

	@Mock
	private ProductFilterRepository productFilterRepository;

	@Mock
	private ProductService productService;

	@Mock
	private FilterService filterService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("제품 ID로 필터 조회 시 FiltersInProduct를 반환해야 한다")
	void testGetProductFilters() {
		// Given
		Long productId = 1L;
		Product mockProduct = mock(Product.class);
		Filter filter1 = Filter.of(FilterName.CILANTRO);
		Filter filter2 = Filter.of(FilterName.CONVENIENCE_STORE);
		List<ProductFilter> mockProductFilters = List.of(
			ProductFilter.of(mockProduct, filter1),
			ProductFilter.of(mockProduct, filter2)
		);
		when(mockProduct.getId()).thenReturn(productId);
		when(productFilterRepository.findByProduct_Id(productId)).thenReturn(mockProductFilters);

		// When
		List<FilterResponseDto> result = productFilterService.getProductFilters(productId);

		// Then
		assertNotNull(result);
		verify(productFilterRepository).findByProduct_Id(productId);
	}

	@Test
	@DisplayName("새로운 제품 필터 생성 시 FiltersInProduct를 반환해야 한다")
	void testCreateProductFilter() {
		// Given
		Long productId = 1L;
		List<Long> filterIds = List.of(1L, 2L);
		Product mockProduct = mock(Product.class);
		Filter filter1 = Filter.of(FilterName.CILANTRO);
		Filter filter2 = Filter.of(FilterName.CONVENIENCE_STORE);

		when(productService.getProductEntityById(productId)).thenReturn(mockProduct);
		when(filterService.getFilterEntityById(1L)).thenReturn(filter1);
		when(filterService.getFilterEntityById(2L)).thenReturn(filter2);

		// When
		List<FilterResponseDto> result = productFilterService.createProductFilter(productId, filterIds);

		// Then
		assertNotNull(result);
		verify(productService).getProductEntityById(productId);
		verify(filterService, times(2)).getFilterEntityById(anyLong());
		verify(productFilterRepository).saveAll(anyList());
	}

	@Test
	@DisplayName("제품 필터 업데이트 시 기존 필터를 삭제하고 새 필터를 생성해야 한다")
	void testUpdateProductFilter() {
		// Given
		Long productId = 1L;
		Product mockProduct = mock(Product.class);
		Filter filter1 = Filter.of(FilterName.CILANTRO);
		List<ProductFilter> existingFilters = List.of(
			ProductFilter.of(mockProduct, filter1)
		);
		Filter filter2 = Filter.of(FilterName.CONVENIENCE_STORE);
		List<Long> newFilterIds = List.of(2L);
		List<FilterResponseDto> expectedResponse = List.of(
			FilterResponseDto.from(filter2)
		);

		when(productFilterRepository.findByProduct_Id(productId)).thenReturn(existingFilters);
		when(productService.getProductEntityById(productId)).thenReturn(mockProduct);
		when(filterService.getFilterEntityById(2L)).thenReturn(filter2);

		// ProductFilter 생성을 모의
		ProductFilter newProductFilter = ProductFilter.of(mockProduct, filter2);
		when(productFilterRepository.saveAll(anyList())).thenReturn(List.of(newProductFilter));

		// When
		List<FilterResponseDto> result = productFilterService.updateProductFilter(productId, newFilterIds);

		// Then
		assertNotNull(result);
		assertEquals(expectedResponse.size(), result.size());
		assertEquals(expectedResponse.get(0).filterId(), result.get(0).filterId());

		// 기존 필터 삭제 검증
		verify(productFilterRepository).findByProduct_Id(productId);
		verify(productFilterRepository).deleteAll(existingFilters);

		// 새 필터 생성 검증
		verify(productService).getProductEntityById(productId);
		verify(filterService).getFilterEntityById(2L);
		verify(productFilterRepository).saveAll(anyList());

		// 추가 검증 (선택적)
		verifyNoMoreInteractions(productFilterRepository);
	}
}
