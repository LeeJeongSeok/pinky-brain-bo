package com.jeongseok.pinkybrainbo.product.util;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.ProductDto;

public class ProductMapper {

	public static Product toProduct(ProductDto.Request createProductRequest) {
		return Product.builder()
			.name(createProductRequest.getName())
			.category(createProductRequest.getCategory())
			.description(createProductRequest.getDescription())
			.build();
	}

	public static ProductDto.Response toDto(Product savedProduct) {
		return ProductDto.Response.builder()
			.name(savedProduct.getName())
			.category(savedProduct.getCategory())
			.description(savedProduct.getDescription())
			.build();
	}
}
