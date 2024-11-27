package com.jeongseok.pinkybrainbo.product.service;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.CreateProductDto;
import com.jeongseok.pinkybrainbo.product.repository.ProductRepository;
import com.jeongseok.pinkybrainbo.product.util.ProductMapper;
import com.jeongseok.pinkybrainbo.product_image.FileStore;
import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import com.jeongseok.pinkybrainbo.product_image.util.ProductImageMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final FileStore fileStore;

	public void createProduct(CreateProductDto createProductDto) throws IOException {

		List<ProductImageDto> productImageDtos = fileStore.storeFiles(createProductDto.getImageFiles());

		// Product 생성
		Product product = ProductMapper.toProduct(createProductDto);

		// ProductImage 변환 및 연관 관계 설정
		List<ProductImage> productImages = ProductImageMapper.toProductImages(productImageDtos);
		product.addProductImage(productImages); // 연관 관계 설정


		productRepository.save(product);
	}
}
