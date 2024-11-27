package com.jeongseok.pinkybrainbo.product_image.util;

import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import java.util.ArrayList;
import java.util.List;

public class ProductImageMapper {

	public static List<ProductImage> toProductImages(List<ProductImageDto> productImageDtos) {
		List<ProductImage> productImages = new ArrayList<>();
		int order = 1; // 상품별로 순서를 초기화

		for (ProductImageDto productImageDto : productImageDtos) {
			productImages.add(toProductImage(productImageDto, order++));
		}

		return productImages;
	}

	private static ProductImage toProductImage(ProductImageDto productImageDto, int order) {
		return ProductImage.builder()
			.imageUrl(productImageDto.getStoreFileName())
			.imageOrder(order)
			.build();
	}
}
