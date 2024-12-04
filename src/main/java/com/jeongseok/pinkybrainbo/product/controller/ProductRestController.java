package com.jeongseok.pinkybrainbo.product.controller;

import com.jeongseok.pinkybrainbo.common.ApiResponse;
import com.jeongseok.pinkybrainbo.product.dto.ProductDto;
import com.jeongseok.pinkybrainbo.product.service.ProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductRestController {

	private final ProductService productService;

	@PostMapping(value = "/products")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<ProductDto.Response> createProduct(@Valid @ModelAttribute ProductDto.Request createProductRequest) throws IOException {

		ProductDto.Response productDto = productService.createProduct(createProductRequest);

		return ApiResponse.created(productDto);

	}

	@GetMapping(value = "/products")
	@ResponseStatus(HttpStatus.OK)
	public void getProducts() {

		productService.getProducts();
	}
}
