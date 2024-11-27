package com.jeongseok.pinkybrainbo.product_image;

import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStore {

	@Value("${file.dir}")
	private String fileDir;

	public String getFullPath(String filename) {
		return fileDir + filename;
	}

	// 여기서 멀티파트로 받아야하는건 확실하니, 엔티티로 바로 저장하지말고 dto로 저장하자
	public List<ProductImageDto> storeFiles(List<MultipartFile> files) throws IOException {
		List<ProductImageDto> storeFileResult = new ArrayList<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				storeFileResult.add(storeFile(file));
			}
		}
		return storeFileResult;
	}

	public ProductImageDto storeFile(MultipartFile file) throws IOException {

		if (file.isEmpty()) {
			return null;
		}

		String originalFilename = file.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);
		file.transferTo(new File(getFullPath(storeFileName)));

		return new ProductImageDto(originalFilename, storeFileName);
	}

	private String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();

		return uuid + "." + ext;
	}

	private String extractExt(String originalFilename) {
		int position = originalFilename.lastIndexOf(".");

		return originalFilename.substring(position + 1);
	}

}
