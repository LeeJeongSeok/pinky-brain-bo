package com.jeongseok.pinkybrainbo.product_image;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jeongseok.pinkybrainbo.common.S3Config;
import com.jeongseok.pinkybrainbo.product_image.dto.ProductImageDto;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileStore {

	private final S3Config s3Config;

	@Value("${file.dir}")
	private String fileDir;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;


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

		// 로컬에 파일 저장
		File localFile = new File(getFullPath(storeFileName));
		file.transferTo(localFile);

		// S3에 파일 업로드
		String s3UploadFileName = putS3(storeFileName, localFile);

		return new ProductImageDto(originalFilename, s3UploadFileName);

	}

	private String putS3(String storeFileName, File localFile) {
		s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, storeFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return s3Config.amazonS3Client().getUrl(bucket, storeFileName).toString();
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
