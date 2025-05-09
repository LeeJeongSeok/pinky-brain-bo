package com.jeongseok.pinkybrainbo.service;

import static com.jeongseok.pinkybrainbo.global.exception.ErrorCode.NOT_FOUND_PRODUCT;
import static com.jeongseok.pinkybrainbo.global.exception.ErrorCode.NOT_FOUND_PRODUCT_IMAGE;

import com.jeongseok.pinkybrainbo.domain.product.Product;
import com.jeongseok.pinkybrainbo.domain.productimage.ProductImage;
import com.jeongseok.pinkybrainbo.dto.ProductImageMapper;
import com.jeongseok.pinkybrainbo.dto.ProductMapper;
import com.jeongseok.pinkybrainbo.dto.productimage.ProductImageCreateDto;
import com.jeongseok.pinkybrainbo.dto.product.ProductCreateDto;
import com.jeongseok.pinkybrainbo.dto.product.ProductUpdateDto;
import com.jeongseok.pinkybrainbo.dto.product.ProductResponse;
import com.jeongseok.pinkybrainbo.global.exception.model.NotFoundException;
import com.jeongseok.pinkybrainbo.repository.ProductImageRepository;
import com.jeongseok.pinkybrainbo.repository.ProductRepository;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductImageRepository productImageRepository;
	private final S3FileUploader s3FileUploader;

	public ProductResponse createProduct(ProductCreateDto productCreateDto) throws IOException {
		// ProductImage S3 업로드
		List<ProductImageCreateDto> productImageDtos = s3FileUploader.storeFiles(productCreateDto.getImageFiles());
		// ProductImageDto -> ProductImage 변환
		List<ProductImage> productImages = ProductImageMapper.toDomain(productImageDtos);
		// Product 생성
		Product product = ProductMapper.toDomain(productCreateDto);

		// ProductImage 테이블에 Product ID(fk) 값 지정
		for (ProductImage productImage : productImages) {
			productImage.addProduct(product);
		}

		return ProductMapper.toResponse(productRepository.save(product));
	}

	@Transactional(readOnly = true)
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

	@Transactional(readOnly = true)
	public ProductResponse getProduct(long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT));

		return ProductMapper.toResponse(product);
	}

	@Transactional
	public void updateProduct(long id, ProductUpdateDto productUpdateDto) throws IOException {
		// 기존 상품 이미지 조회
		List<ProductImage> existingImages = productImageRepository.findByProduct_Id(id)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_IMAGE));

		// 기존 상품 조회
		Product savedProduct = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT));

		// 삭제할 이미지
		List<Long> imagesToDeleteId = productUpdateDto.getImagesToDelete();
		List<MultipartFile> newImages = productUpdateDto.getNewImages();

		// 이미지 파일을 삭제하는 경우
		if (imagesToDeleteId != null && newImages == null) {
			// 기존 상품 이미지에서 삭제할 이미지를 필터링
			List<Long> deleteList = existingImages.stream()
				.filter(existingImage -> imagesToDeleteId.contains(existingImage.getId()))
				.map(ProductImage::getId)
				.toList();

			// 삭제된 이미지 삭제 쿼리 진행
			for (Long imageId : deleteList) {
				productImageRepository.deleteById(imageId);
			}
		}

		// 이미지 파일을 추가할 경우
		if (imagesToDeleteId == null && newImages != null) {
			// 새롭게 추가될 이미지
			// 추가될 때, 기존 이미지의 순서와 맞게 업로드 되어야한다.
			int maxOrder = existingImages.stream()
				.mapToInt(ProductImage::getImageOrder)
				.max()
				.orElse(0);

			// 새로운 이미지 파일 저장
			List<ProductImageCreateDto> updateProductImageDtos = s3FileUploader.storeFiles(
				productUpdateDto.getNewImages());
			List<ProductImage> saveNewImages = ProductImageMapper.toDomain(updateProductImageDtos, maxOrder);

			// product_id cannot be null
			for (ProductImage newImage : saveNewImages) {
				newImage.addProduct(savedProduct);
				productImageRepository.save(newImage);
			}
		}

		// 이미지 파일을 삭제 및 추가를 동시에 하는 경우
		if (imagesToDeleteId != null && newImages != null) {
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
			List<ProductImageCreateDto> updateProductImageDtos = s3FileUploader.storeFiles(
				productUpdateDto.getNewImages());
			List<ProductImage> saveNewImages = ProductImageMapper.toDomain(updateProductImageDtos, maxOrder);

			// product_id cannot be null
			for (ProductImage newImage : saveNewImages) {
				newImage.addProduct(savedProduct);
				productImageRepository.save(newImage);
			}
		}

		// 상품 정보 업데이트
		savedProduct.updateProduct(productUpdateDto);
	}

	@Transactional
	public void deleteProduct(long id) {
		Product savedProduct = productRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT));

		productRepository.delete(savedProduct);
	}
}
