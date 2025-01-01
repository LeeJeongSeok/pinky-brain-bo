package com.jeongseok.pinkybrainbo.product.service;

import com.jeongseok.pinkybrainbo.product.domain.Product;
import com.jeongseok.pinkybrainbo.product.dto.ProductDetailDto;
import com.jeongseok.pinkybrainbo.product.dto.request.AddProductRequest;
import com.jeongseok.pinkybrainbo.product.dto.request.ModifyProductRequest;
import com.jeongseok.pinkybrainbo.product.dto.response.ProductResponse;
import com.jeongseok.pinkybrainbo.product.repository.ProductRepository;
import com.jeongseok.pinkybrainbo.product.util.ProductMapper;
import com.jeongseok.pinkybrainbo.product_image.FileStore;
import com.jeongseok.pinkybrainbo.product_image.domain.ProductImage;
import com.jeongseok.pinkybrainbo.product_image.dto.request.AddProductImageRequest;
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
	public ProductResponse createProduct(AddProductRequest addProductRequest) throws IOException {

		// ProductImage S3 업로드
		List<AddProductImageRequest> productImageDtos = fileStore.storeFiles(addProductRequest.getImageFiles());
		// ProductImageDto -> ProductImage 변환
		List<ProductImage> productImages = ProductImageMapper.toDomain(productImageDtos);
		// Product 생성
		Product product = ProductMapper.toDomain(addProductRequest);

		// ProductImage 테이블에 Product ID(fk) 값 지정
		for (ProductImage productImage : productImages) {
			productImage.addProduct(product);
		}

		Product savedProduct = productRepository.save(product);

		return ProductMapper.toResponse(savedProduct);

	}


	public Page<ProductResponse> getPaginatedProducts(Pageable pageable, String searchKeyword) {

		// 페이지네이션 기본 정보 설정
		int pageSize = pageable.getPageSize(); // 한 페이지당 항목 수
		int currentPage = pageable.getPageNumber(); // 현재 페이지 번호 (0부터 시작)
		int startItem = currentPage * pageSize; // 현재 페이지의 시작 항목 인덱스

		// ProductDto.Response가 변경되므로 해당 부분에서 페이징 처리가 되지 않음
		List<ProductResponse> products = productRepository.findProductBySearchKeyword(searchKeyword)
			.stream()
			.map(ProductMapper::toResponse)
			.collect(Collectors.toList());

		List<ProductResponse> productPages;

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

	public ProductResponse getProduct(long id) {

		Product product = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 데이터가 없쪄요"));

		return ProductMapper.toResponse(product);
	}

	@Transactional
	public ProductResponse updateProduct(long id, ModifyProductRequest modifyProductRequest) throws IOException {

		// 기존 상품 이미지 조회
		List<ProductImage> existingImages = productImageRepository.findByProduct_Id(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 이미지 데이터가 없습니다."));

		// 기존 상품 조회
		Product savedProduct = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 데이터가 없습니다."));

		// 삭제할 이미지
		// TODO: 삭제할 이미지가 없는 경우에 대한 로직 처리
		List<Long> imagesToDeleteId = modifyProductRequest.getImagesToDelete();

		// 기존 상품 이미지에서 삭제할 이미지를 필터링
		List<Long> deleteList = existingImages.stream()
			.filter(existingImage -> imagesToDeleteId.contains(existingImage.getId()))
			.map(ProductImage::getId)
			.toList();

		// 필터링 대상 이후 살아남은 이미지 필터링
		List<ProductImage> updatedImages = existingImages.stream()
			.filter(existingImage -> !imagesToDeleteId.contains(existingImage.getId()))
			.toList();

		// 삭제된 이미지 삭제 쿼리 진행
		for (Long imageId : deleteList) {
			productImageRepository.deleteById(imageId);
		}


		// 새롭게 추가될 이미지
		// 추가될 때, 기존 이미지의 순서와 맞게 업로드 되어야한다.
		// 살아남은 이미지의 order 값 중 최대값 가져온다.
		// 살아남은 이미지의 order 값 중 최대값 가져오기
		int maxOrder = updatedImages.stream()
			.mapToInt(ProductImage::getImageOrder)
			.max()
			.orElse(0); // 살아남은 이미지가 없을 경우 기본값 0

		// 새로운 이미지 파일 저장
		List<AddProductImageRequest> updateProductImageDtos = fileStore.storeFiles(modifyProductRequest.getNewImages());
		List<ProductImage> newImages = ProductImageMapper.toDomain(updateProductImageDtos, maxOrder);

		// product_id cannot be null
		for (ProductImage newImage : newImages) {
			newImage.addProduct(savedProduct);
			productImageRepository.save(newImage);
		}

		// 상품 정보 업데이트
		savedProduct.updateProduct(modifyProductRequest);

		return null;
	}

	@Transactional
	public void deleteProduct(long id) {

		// 기존 상품 조회
		Product savedProduct = productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("상품 데이터가 없습니다."));

		productRepository.delete(savedProduct);
	}
}
