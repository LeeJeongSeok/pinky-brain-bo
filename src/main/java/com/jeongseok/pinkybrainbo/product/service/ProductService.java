package com.jeongseok.pinkybrainbo.product.service;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.ProductDto;
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

	// TODO: Response Entity에 맞춰서 값을 리턴할 수 있도록 고려해야함
	public ProductDto.Response createProduct(ProductDto.Request createProductRequest) throws IOException {

		List<ProductImageDto.Request> productImageDtos = fileStore.storeFiles(createProductRequest.getImageFiles());
		// Product 생성
		Product product = ProductMapper.toProduct(createProductRequest);
		// ProductImage 변환 및 연관 관계 설정
		List<ProductImage> productImages = ProductImageMapper.toProductImages(productImageDtos);
		product.addProductImage(productImages); // 연관 관계 설정

		Product savedProduct = productRepository.save(product);

		return ProductMapper.toDto(savedProduct);

	}

	public void getProducts() {

	}
}
