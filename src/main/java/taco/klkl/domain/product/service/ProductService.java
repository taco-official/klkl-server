package taco.klkl.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.service.SubcategoryService;
import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.region.domain.City;
import taco.klkl.domain.region.service.CityServiceImpl;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.util.UserUtil;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final CityServiceImpl cityServiceImpl;
	private final UserUtil userUtil;
	private final SubcategoryService subcategoryService;

	public List<ProductSimpleResponseDto> getAllProducts(Pageable pageable) {
		return productRepository.findAll(pageable).stream()
			.map(ProductSimpleResponseDto::from)
			.toList();
	}

	public ProductDetailResponseDto getProductById(final Long id) {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto createProduct(final ProductCreateRequestDto productDto) {
		final Product product = createProductEntity(productDto);
		productRepository.save(product);
		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public ProductDetailResponseDto updateProduct(final Long id, final ProductUpdateRequestDto productDto) {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);

		City city = getCityEntity(productDto.cityId());
		Subcategory subcategory = getSubcategoryEntity(productDto.subcategoryId());

		product.update(
			productDto.name(),
			productDto.description(),
			productDto.address(),
			productDto.price(),
			city,
			subcategory,
			productDto.currencyId()
		);

		return ProductDetailResponseDto.from(product);
	}

	@Transactional
	public void deleteProduct(final Long id) {
		final Product product = productRepository.findById(id)
			.orElseThrow(ProductNotFoundException::new);
		productRepository.delete(product);
	}

	private Product createProductEntity(final ProductCreateRequestDto productDto) {
		final User user = userUtil.findTestUser();
		final City city = getCityEntity(productDto.cityId());
		final Subcategory subcategory = getSubcategoryEntity(productDto.subcategoryId());

		return Product.of(
			user,
			productDto.name(),
			productDto.description(),
			productDto.address(),
			productDto.price(),
			city,
			subcategory,
			productDto.currencyId()
		);
	}

	private City getCityEntity(final Long cityId) {
		if (cityId != null) {
			return cityServiceImpl.getCityById(cityId);
		}
		return null;
	}

	private Subcategory getSubcategoryEntity(final Long subcategoryId) {
		if (subcategoryId != null) {
			return subcategoryService.getSubcategoryById(subcategoryId);
		}
		return null;
	}
}
