package com.jeongseok.pinkybrainbo.product_image.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductImageResponse {

	private long id;
	private String imageUrl;
	private int imageOrder;

}
