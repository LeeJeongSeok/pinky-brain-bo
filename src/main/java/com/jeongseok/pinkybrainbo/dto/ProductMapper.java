package com.jeongseok.pinkybrainbo.dto;

import com.jeongseok.pinkybrainbo.domain.Product;
import com.jeongseok.pinkybrainbo.dto.product.ProductCreateDto;
import com.jeongseok.pinkybrainbo.dto.product.ProductResponse;
import java.util.ArrayList;

public class ProductMapper {

	public static Product toDomain(ProductCreateDto productCreateDto) {
		return Product.builder()
			.name(productCreateDto.getName())
			.category(productCreateDto.getCategory())
			.description(productCreateDto.getDescription())
			.productImages(new ArrayList<>())
			.build();
	}

	public static ProductResponse toResponse(Product product) {
		return ProductResponse.builder()
			.id(product.getId())
			.name(product.getName())
			.category(product.getCategory())
			.description(product.getDescription())
			.imageFiles(ProductImageMapper.toResponse(product.getProductImages()))
			.createdAt(product.getCreatedAt())
			.updatedAt(product.getUpdatedAt())
			.build();
	}
}
