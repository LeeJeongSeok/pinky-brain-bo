package com.jeongseok.pinkybrainbo.dto.product;

import com.jeongseok.pinkybrainbo.dto.productimage.ProductImageResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

	private long id;
	private String name;
	private String category;
	private String description;
	private List<ProductImageResponse> imageFiles;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


}
