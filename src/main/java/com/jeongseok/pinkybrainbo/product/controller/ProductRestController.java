package com.jeongseok.pinkybrainbo.product.controller;

import com.jeongseok.pinkybrainbo.product.dto.CreateProductDto;
import com.jeongseok.pinkybrainbo.product.service.ProductService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Map<String, Object>> createProduct(@Valid @ModelAttribute CreateProductDto createProductDto) throws IOException {

		long productId = productService.createProduct(createProductDto);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "상품 등록 성공");
		response.put("productId", productId);

		return ResponseEntity.ok(response);

	}
}
