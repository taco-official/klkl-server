package taco.klkl.domain.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.request.ProductUpdateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.service.ProductService;

@RestController
@RequestMapping("/v1/products")
@Tag(name = "2. 상품", description = "상품 관련 API")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/{id}")
	@Operation(summary = "상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
	public ResponseEntity<ProductDetailResponseDto> getProductInfoById(
		@PathVariable Long id
	) {
		ProductDetailResponseDto productDto = productService.getProductInfoById(id);
		return ResponseEntity.ok().body(productDto);
	}

	@PostMapping
	@Operation(summary = "상품 등록", description = "상품을 등록합니다.")
	public ResponseEntity<ProductDetailResponseDto> createProduct(
		@Valid @RequestBody ProductCreateRequestDto createRequest
	) {
		ProductDetailResponseDto productDto = productService.createProduct(createRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "상품 정보 수정", description = "상품 정보를 수정합니다.")
	public ResponseEntity<ProductDetailResponseDto> updateProduct(
		@PathVariable Long id,
		@Valid @RequestBody ProductUpdateRequestDto updateRequest
	) {
		ProductDetailResponseDto productDto = productService.updateProduct(id, updateRequest);
		return ResponseEntity.ok().body(productDto);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
	public ResponseEntity<ProductDetailResponseDto> deleteProduct(
		@PathVariable Long id
	) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
