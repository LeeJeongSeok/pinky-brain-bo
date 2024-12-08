package com.jeongseok.pinkybrainbo.product.service;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.ProductDto;
import com.jeongseok.pinkybrainbo.product.repository.ProductRepository;
import com.jeongseok.pinkybrainbo.product.util.ProductMapper;
import com.jeongseok.pinkybrainbo.product_image.FileStore;
import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import com.jeongseok.pinkybrainbo.product_image.repository.ProductImageRepository;
import com.jeongseok.pinkybrainbo.product_image.util.ProductImageMapper;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final FileStore fileStore;

	// TODO: Response Entity에 맞춰서 값을 리턴할 수 있도록 고려해야함
	public ProductDto.Response createProduct(ProductDto.Request createProductRequest) throws IOException {

		List<ProductImageDto.Request> productImageDtos = fileStore.storeFiles(createProductRequest.getImageFiles());
		// Product 생성
		Product product = ProductMapper.toProduct(createProductRequest);
		// ProductImage 변환 및 연관 관계 설정
		List<ProductImage> productImages = ProductImageMapper.toProductImages(productImageDtos);

		for (ProductImage productImage : productImages) {
			productImage.addProduct(product);
		}

		Product savedProduct = productRepository.save(product);

		return ProductMapper.toDto(savedProduct);

	}

	public List<ProductDto.Response> getProducts() {
		return productRepository.findAll()
			.stream()
			.map(ProductMapper::toDto)
			.collect(Collectors.toList());
	}

	public Page<ProductDto.Response> getPaginatedProducts(Pageable pageable, String searchKeyword) {

		// 페이지네이션 기본 정보 설정
		int pageSize = pageable.getPageSize(); // 한 페이지당 항목 수
		int currentPage = pageable.getPageNumber(); // 현재 페이지 번호 (0부터 시작)
		int startItem = currentPage * pageSize; // 현재 페이지의 시작 항목 인덱스

		List<ProductDto.Response> products = productRepository.findProductBySearchKeyword(searchKeyword)
			.stream()
			.map(ProductMapper::toDto)
			.collect(Collectors.toList());

		List<ProductDto.Response> productPages;

		// 현재 페이지의 시작 인덱스가 전체 리스트 크기보다 큰 경우 빈 리스트 반환
		if (products.size() < startItem) {
			productPages = Collections.emptyList();
		} else {
			// 현재 페이지에 해당하는 서브리스트 생성
			int toIndex = Math.min(startItem + pageSize, products.size());
			productPages = products.subList(startItem, toIndex);
		}

		// 페이지네이션 결과를 PageImpl 객체로 반환
		return new PageImpl<>(productPages, PageRequest.of(currentPage, pageSize), products.size());
	}

	public ProductDto.Response getProduct(long id) {

		Product product = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 데이터가 없쪄요"));

		return ProductMapper.toDto(product);
	}

	@Transactional
	public ProductDto.Response updateProduct(long id, ProductDto.Request updateProductRequest) throws IOException {

		List<ProductImageDto.Request> updateProductImageDtos = fileStore.storeFiles(updateProductRequest.getImageFiles());
		List<ProductImage> updateProductImages = ProductImageMapper.toProductImages(updateProductImageDtos);

		Product savedProduct = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 데이터가 없쪄요"));

		List<ProductImage> savedProductImages = productImageRepository.findByProduct_Id(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 이미지 데이터가 없쪄요!"));

		// 기존 이미지 삭제 (데이터베이스에서 삭제)
		productImageRepository.deleteAll(savedProductImages);
		// 연관 관계에서 제거
		savedProduct.getProductImages().clear();

		// 새로운 이미지 추가
		for (ProductImage image : updateProductImages) {
			image.addProduct(savedProduct); // 연관 관계 설정
		}

		savedProduct.update(ProductMapper.toProduct(updateProductRequest));

		return ProductMapper.toDto(savedProduct);

	}
}
