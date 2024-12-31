package com.jeongseok.pinkybrainbo.product_image.util;

import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import com.jeongseok.pinkybrainbo.product_image.dto.request.AddProductImageRequest;
import java.util.ArrayList;
import java.util.List;

public class ProductImageMapper {

	// 생성
	public static List<ProductImage> toDomain(List<AddProductImageRequest> productImageDtos) {
		List<ProductImage> productImages = new ArrayList<>();
		int order = 1; // 상품별로 순서를 초기화

		for (AddProductImageRequest productImageDto : productImageDtos) {
			productImages.add(toDomain(productImageDto, order++));
		}

		return productImages;
	}

	// 수정
	public static List<ProductImage> toDomain(List<AddProductImageRequest> productImageDtos, int order) {
		List<ProductImage> productImages = new ArrayList<>();

		for (AddProductImageRequest productImageDto : productImageDtos) {
			productImages.add(toDomain(productImageDto, ++order));
		}

		return productImages;
	}

	public static List<ProductImageDto.Response> toDto(List<ProductImage> productImages) {
		List<ProductImageDto.Response> productImageDtos = new ArrayList<>();

		for (ProductImage productImage : productImages) {
			productImageDtos.add(toDto(productImage));
		}

		return productImageDtos;
	}

	private static ProductImage toDomain(AddProductImageRequest addProductImageRequest, int order) {
		return ProductImage.builder()
			.imageUrl(addProductImageRequest.getStoreFileName())
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


}
