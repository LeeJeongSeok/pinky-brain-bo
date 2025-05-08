package com.jeongseok.pinkybrainbo.dto.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

	private String name;
	private String category;
	private String description;
	private List<Long> imagesToDelete;
	private List<MultipartFile> newImages;

}
