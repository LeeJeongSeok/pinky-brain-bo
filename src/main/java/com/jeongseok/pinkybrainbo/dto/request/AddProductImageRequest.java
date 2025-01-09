package com.jeongseok.pinkybrainbo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductImageRequest {

	private String uploadFileName;
	private String storeFileName;

}
