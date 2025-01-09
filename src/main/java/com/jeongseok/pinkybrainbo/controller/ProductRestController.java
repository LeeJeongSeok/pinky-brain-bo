package com.jeongseok.pinkybrainbo.controller;

import com.jeongseok.pinkybrainbo.common.ApiResponse;
import com.jeongseok.pinkybrainbo.dto.request.AddProductRequest;
import com.jeongseok.pinkybrainbo.dto.request.ModifyProductRequest;
import com.jeongseok.pinkybrainbo.dto.response.ProductResponse;
import com.jeongseok.pinkybrainbo.exception.SuccessCode;
import com.jeongseok.pinkybrainbo.service.ProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestController {

	private final ProductService productService;

	@PostMapping(value = "/products")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ApiResponse<ProductResponse> createProduct(@Valid @ModelAttribute AddProductRequest addProductRequest) throws IOException {

		return ApiResponse.success(SuccessCode.PRODUCT_CREATE_SUCCESS, productService.createProduct(addProductRequest));
	}

	@GetMapping(value = "/products")
	@ResponseStatus(value = HttpStatus.OK)
	public ApiResponse<List<ProductResponse>> getProducts(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, @RequestParam("keyword") Optional<String> keyword) {

		// 페이징 처리
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(10);
		String searchKeyword = keyword.orElse("");

		Page<ProductResponse> productPage = productService.getPaginatedProducts(PageRequest.of(currentPage - 1, pageSize), searchKeyword);

		// okWithPaging 메서드 사용
		return ApiResponse.successWithPaging(SuccessCode.PRODUCT_LIST_GET_SUCCESS, productPage.getContent(), productPage);
	}

	@GetMapping("/products/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public ApiResponse<ProductResponse> getProduct(@PathVariable("id") long id) {

		ProductResponse productResponse = productService.getProduct(id);

		return ApiResponse.success(SuccessCode.PRODUCT_GET_SUCCESS, productResponse);
	}

	@PatchMapping("/products/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public ApiResponse<ProductResponse> updateProduct(@PathVariable("id") long id, @ModelAttribute ModifyProductRequest modifyProductRequest) throws IOException {

		ProductResponse updateProductDto = productService.updateProduct(id, modifyProductRequest);

		return ApiResponse.success(SuccessCode.PRODUCT_UPDATE_SUCCESS, updateProductDto);
	}

	@DeleteMapping("/products/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public ApiResponse<Void> deleteProduct(@PathVariable("id") long id) {

		productService.deleteProduct(id);

		return ApiResponse.success(SuccessCode.PRODUCT_DELETE_SUCCESS);
	}
}
