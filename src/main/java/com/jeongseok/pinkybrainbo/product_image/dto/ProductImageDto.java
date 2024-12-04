package com.jeongseok.pinkybrainbo.product_image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ProductImageDto {


	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {

		private String uploadFileName;
		private String storeFileName;

	}

	@Getter
	@Builder
	public static class Response {

		private long id;
		private String imageUrl;
		private int imageOrder;

	}


}
