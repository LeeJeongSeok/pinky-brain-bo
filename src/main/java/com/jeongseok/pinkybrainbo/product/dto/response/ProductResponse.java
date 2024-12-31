package com.jeongseok.pinkybrainbo.product.dto.response;

import com.jeongseok.pinkybrainbo.product_image.dto.response.ProductImageResponse;
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

}
