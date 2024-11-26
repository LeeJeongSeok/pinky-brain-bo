package com.jeongseok.pinkybrainbo.file;

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

	public List<UploadFile> storeFiles(List<MultipartFile> files) throws IOException {
		List<UploadFile> storeFileResult = new ArrayList<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				storeFileResult.add(storeFile(file));
			}
		}
		return storeFileResult;
	}

	public UploadFile storeFile(MultipartFile file) throws IOException {

		if (file.isEmpty()) {
			return null;
		}

		String originalFilename = file.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);
		file.transferTo(new File(getFullPath(storeFileName)));

		return new UploadFile(originalFilename, storeFileName);
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
