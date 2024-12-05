package com.jeongseok.pinkybrainbo.product.controller;

import com.jeongseok.pinkybrainbo.common.ApiResponse;
import com.jeongseok.pinkybrainbo.product.dto.ProductDto;
import com.jeongseok.pinkybrainbo.product.service.ProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestController {

	private final ProductService productService;

	@PostMapping(value = "/products")
	public ApiResponse<ProductDto.Response> createProduct(@Valid @ModelAttribute ProductDto.Request createProductRequest) throws IOException {

		ProductDto.Response productDto = productService.createProduct(createProductRequest);

		return ApiResponse.created(productDto);

	}

	@GetMapping(value = "/products")
	public ApiResponse<List<ProductDto.Response>> getProducts(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, @RequestParam("keyword") Optional<String> keyword) {

		// 페이징 처리
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(10);
		String searchKeyword = keyword.orElse("");

		Page<ProductDto.Response> productPage = productService.getPaginatedProducts(PageRequest.of(currentPage - 1, pageSize), searchKeyword);

		// okWithPaging 메서드 사용
		return ApiResponse.okWithPaging(productPage.getContent(), productPage);
	}

	@GetMapping("/products/{id}")
	public ApiResponse<ProductDto.Response> getProduct(@PathVariable("id") long id) {

		ProductDto.Response productResponse = productService.getProduct(id);

		return ApiResponse.ok(productResponse);
	}
}
