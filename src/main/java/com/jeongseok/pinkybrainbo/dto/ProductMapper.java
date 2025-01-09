package com.jeongseok.pinkybrainbo.dto;

import com.jeongseok.pinkybrainbo.domain.Product;
import com.jeongseok.pinkybrainbo.dto.request.AddProductRequest;
import com.jeongseok.pinkybrainbo.dto.response.ProductResponse;
import java.util.ArrayList;

public class ProductMapper {

	public static Product toDomain(AddProductRequest addProductRequest) {
		return Product.builder()
			.name(addProductRequest.getName())
			.category(addProductRequest.getCategory())
			.description(addProductRequest.getDescription())
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
			.build();
	}
}
