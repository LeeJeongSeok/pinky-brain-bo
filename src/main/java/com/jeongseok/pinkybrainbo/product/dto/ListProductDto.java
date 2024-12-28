package com.jeongseok.pinkybrainbo.product.dto;

import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto.Response;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListProductDto {

	private long id;
	private String name;
	private String category;
	private String description;
	private List<Response> imageFiles;

}
