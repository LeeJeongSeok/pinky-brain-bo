package com.jeongseok.pinkybrainbo.product.util;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.CreateProductDto;

public class ProductMapper {

	public static Product toProduct(CreateProductDto createProductDto) {
		return Product.builder()
			.name(createProductDto.getName())
			.description(createProductDto.getDescription())
			.build();
	}

}
