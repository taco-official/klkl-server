package taco.klkl.domain.product.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.request.ProductFilterOptionsDto;
import taco.klkl.domain.product.dto.response.PagedResponseDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.global.common.constants.ProductConstants;

@RestController
@RequestMapping("/v1/products")
@Tag(name = "2. 상품", description = "상품 관련 API")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	@Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다.")
	public PagedResponseDto<ProductSimpleResponseDto> getProducts(
		@PageableDefault(size = ProductConstants.DEFAULT_PAGE_SIZE) Pageable pageable,
		@RequestParam(name = "country_id", required = false) List<Long> countryIds,
		@RequestParam(name = "city_id", required = false) List<Long> cityIds
	) {
		ProductFilterOptionsDto filterOptions = new ProductFilterOptionsDto(
			countryIds,
			cityIds
		);
		return productService.getProductsByFilterOptions(pageable, filterOptions);
	}

	@GetMapping("/{id}")
	@Operation(summary = "상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
	public ProductDetailResponseDto getProductById(
		@PathVariable Long id
	) {
		return productService.getProductById(id);
	}

	@PostMapping
	@Operation(summary = "상품 등록", description = "상품을 등록합니다.")
	@ResponseStatus(HttpStatus.CREATED)
	public ProductDetailResponseDto createProduct(
		@Valid @RequestBody ProductCreateUpdateRequestDto createRequest
	) {
		return productService.createProduct(createRequest);
	}

	@PutMapping("/{id}")
	@Operation(summary = "상품 정보 수정", description = "상품 정보를 수정합니다.")
	public ProductDetailResponseDto updateProduct(
		@PathVariable Long id,
		@Valid @RequestBody ProductCreateUpdateRequestDto updateRequest
	) {
		return productService.updateProduct(id, updateRequest);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
	public ResponseEntity<Void> deleteProduct(
		@PathVariable Long id
	) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
