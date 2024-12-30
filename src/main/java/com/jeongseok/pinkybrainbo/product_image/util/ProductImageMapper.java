package com.jeongseok.pinkybrainbo.product_image.util;

import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import java.util.ArrayList;
import java.util.List;

public class ProductImageMapper {

	public static List<ProductImage> toProductImages(List<ProductImageDto.Request> productImageDtos) {
		List<ProductImage> productImages = new ArrayList<>();
		int order = 1; // 상품별로 순서를 초기화

		for (ProductImageDto.Request productImageDto : productImageDtos) {
			productImages.add(toProductImage(productImageDto, order++));
		}

		return productImages;
	}

	public static List<ProductImage> toUpdateProductImages(List<ProductImageDto.Request> productImageDtos, int order) {
		List<ProductImage> productImages = new ArrayList<>();

		for (ProductImageDto.Request productImageDto : productImageDtos) {
			productImages.add(toProductImage(productImageDto, ++order));
		}

		return productImages;
	}

	private static ProductImage toProductImage(ProductImageDto.Request productImageDto, int order) {
		return ProductImage.builder()
			.imageUrl(productImageDto.getStoreFileName())
			.imageOrder(order)
			.build();
	}

	private static ProductImageDto.Response toDto(ProductImage productImage) {
		return ProductImageDto.Response.builder()
			.id(productImage.getId())
			.imageUrl(productImage.getImageUrl())
			.imageOrder(productImage.getImageOrder())
			.build();
	}

	public static List<ProductImageDto.Response> toDto(List<ProductImage> productImages) {
		List<ProductImageDto.Response> productImageDtos = new ArrayList<>();

		for (ProductImage productImage : productImages) {
			productImageDtos.add(toDto(productImage));
		}

		return productImageDtos;
	}
}
