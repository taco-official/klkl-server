package taco.klkl.domain.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.exception.SubcategoryNotFoundException;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.response.PagedResponseDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.domain.Currency;
import taco.klkl.domain.region.exception.CityNotFoundException;
import taco.klkl.domain.region.exception.CurrencyNotFoundException;
import taco.klkl.domain.region.service.CityService;
import taco.klkl.domain.region.service.CurrencyService;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	private final CityService cityService;
	private final CurrencyService currencyService;
	private final SubcategoryService subcategoryService;

	private final UserUtil userUtil;

	public PagedResponseDto<ProductSimpleResponseDto> getProducts(Pageable pageable) {
		Page<Product> productPage = productRepository.findAll(pageable);
		return PagedResponseDto.of(productPage, ProductSimpleResponseDto::from);
	}

	public ProductDetailResponseDto getProductById(final Long id) throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto createProduct(final ProductCreateUpdateRequestDto createRequest) {
		final Product product = createProductEntity(createRequest);
		productRepository.save(product);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto updateProduct(final Long id, final ProductCreateUpdateRequestDto updateRequest)
		throws ProductNotFoundException {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		updateProductEntity(product, updateRequest);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public void deleteProduct(final Long id) {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		productRepository.delete(product);
	}

	private Product createProductEntity(final ProductCreateUpdateRequestDto createRequest) {
		final User user = userUtil.findTestUser();
		final City city = getCityEntity(createRequest.cityId());
		final Subcategory subcategory = getSubcategoryEntity(createRequest.subcategoryId());
		final Currency currency = getCurrencyEntity(createRequest.currencyId());

		return Product.of(
			createRequest.name(),
			createRequest.description(),
			createRequest.address(),
			createRequest.price(),
			user,
			city,
			subcategory,
			currency
		);
	}

	private void updateProductEntity(final Product product, final ProductCreateUpdateRequestDto updateRequest) {
		final City city = getCityEntity(updateRequest.cityId());
		final Subcategory subcategory = getSubcategoryEntity(updateRequest.subcategoryId());
		final Currency currency = getCurrencyEntity(updateRequest.currencyId());

		product.update(
			updateRequest.name(),
			updateRequest.description(),
			updateRequest.address(),
			updateRequest.price(),
			city,
			subcategory,
			currency
		);
	}

	private City getCityEntity(final Long cityId) throws CityNotFoundException {
		return cityService.getCityById(cityId);
	}

	private Subcategory getSubcategoryEntity(final Long subcategoryId) throws SubcategoryNotFoundException {
		return subcategoryService.getSubcategoryById(subcategoryId);
	}

	private Currency getCurrencyEntity(final Long currencyId) throws CurrencyNotFoundException {
		return currencyService.getCurrencyById(currencyId);
	}
}
