package com.jeongseok.pinkybrainbo.dto.productimage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageCreateDto {

	private String uploadFileName;
	private String storeFileName;

}
