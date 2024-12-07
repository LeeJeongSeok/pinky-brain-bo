package com.jeongseok.pinkybrainbo.product.util;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.ProductDto;
import com.jeongseok.pinkybrainbo.product_image.util.ProductImageMapper;

public class ProductMapper {

	public static Product toProduct(ProductDto.Request createProductRequest) {
		return Product.builder()
			.name(createProductRequest.getName())
			.category(createProductRequest.getCategory())
			.description(createProductRequest.getDescription())
			.build();
	}

	public static ProductDto.Response toDto(Product product) {
		return ProductDto.Response.builder()
			.id(product.getId())
			.name(product.getName())
			.category(product.getCategory())
			.description(product.getDescription())
			.imageFiles(ProductImageMapper.toDto(product.getProductImages()))
			.build();
	}
}
