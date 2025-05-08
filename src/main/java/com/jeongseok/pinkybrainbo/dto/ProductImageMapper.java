package com.jeongseok.pinkybrainbo.dto;

import com.jeongseok.pinkybrainbo.domain.ProductImage;
import com.jeongseok.pinkybrainbo.dto.productimage.ProductImageCreateDto;
import com.jeongseok.pinkybrainbo.dto.productimage.ProductImageResponse;
import java.util.ArrayList;
import java.util.List;

public class ProductImageMapper {

	// 생성
	public static List<ProductImage> toDomain(List<ProductImageCreateDto> productImageDtos) {
		List<ProductImage> productImages = new ArrayList<>();
		int order = 1; // 상품별로 순서를 초기화

		for (ProductImageCreateDto productImageDto : productImageDtos) {
			productImages.add(toDomain(productImageDto, order++));
		}

		return productImages;
	}

	// 수정
	public static List<ProductImage> toDomain(List<ProductImageCreateDto> productImageDtos, int order) {
		List<ProductImage> productImages = new ArrayList<>();

		for (ProductImageCreateDto productImageDto : productImageDtos) {
			productImages.add(toDomain(productImageDto, ++order));
		}

		return productImages;
	}

	public static List<ProductImageResponse> toResponse(List<ProductImage> productImages) {
		List<ProductImageResponse> productImageDtos = new ArrayList<>();

		for (ProductImage productImage : productImages) {
			productImageDtos.add(toResponse(productImage));
		}

		return productImageDtos;
	}

	private static ProductImage toDomain(ProductImageCreateDto productImageCreateDto, int order) {
		return ProductImage.builder()
			.imageUrl(productImageCreateDto.getStoreFileName())
			.imageOrder(order)
			.build();
	}

	private static ProductImageResponse toResponse(ProductImage productImage) {
		return ProductImageResponse.builder()
			.id(productImage.getId())
			.imageUrl(productImage.getImageUrl())
			.imageOrder(productImage.getImageOrder())
			.build();
	}


}
