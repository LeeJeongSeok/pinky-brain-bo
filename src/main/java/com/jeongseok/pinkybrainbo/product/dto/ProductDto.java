package com.jeongseok.pinkybrainbo.product.dto;

import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ProductDto {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Request {

		@NotBlank(message = "상품 이름은 필수 입력 값입니다.")
		private String name;

		@NotBlank(message = "상품 카테고리는 필수 입력 값입니다.")
		private String category;

		@NotBlank(message = "상품 설명은 필수 입력 값입니다.")
		private String description;

		private List<MultipartFile> imageFiles;


		@Override
		public String toString() {
			return "CreateProductDto{" +
				"name='" + name + '\'' +
				", category='" + category + '\'' +
				", description='" + description + '\'' +
				", imageFiles=" + imageFiles +
				'}';
		}
	}

	@Getter
	@Builder
	public static class Response {

		private long id;
		private String name;
		private String category;
		private String description;

	}

}
