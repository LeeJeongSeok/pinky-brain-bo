package com.jeongseok.pinkybrainbo.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeongseok.pinkybrainbo.exception.ErrorCode;
import com.jeongseok.pinkybrainbo.exception.SuccessCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

	private final int status;
	private final boolean success;
	private final String message;
	private T data;
	private PageInfo pageInfo;

	public static <T> ApiResponse<T> success(SuccessCode successCode) {
		return new ApiResponse<>(successCode.getHttpStatus().value(), successCode.isSuccess(),
			successCode.getMessage(), null, null);
	}

	public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
		return new ApiResponse<>(successCode.getHttpStatus().value(), successCode.isSuccess(), successCode.getMessage(),
			data, null);
	}

	// 페이징 전용 정적 메서드 추가
	public static <T> ApiResponse<T> successWithPaging(SuccessCode successCode, T data, Page<?> page) {
		PageInfo pageInfo = PageInfo.of(page);
		return new ApiResponse<>(successCode.getHttpStatus().value(), successCode.isSuccess(), successCode.getMessage(), data, pageInfo);
	}

	public static <T> ApiResponse<T> error(ErrorCode errorCode) {
		return new ApiResponse<>(errorCode.getHttpStatus().value(), errorCode.isSuccess(),
			errorCode.getMessage(), null, null);
	}

	// 페이지 정보를 담는 내부 레코드
	@Getter
	@AllArgsConstructor
	private static class PageInfo {
		private int currentPage;
		private int pageSize;
		private int totalPages;
		private long totalElements;
		private boolean hasNext;
		private boolean hasPrevious;

		public static PageInfo of(Page<?> page) {
			return new PageInfo(
				page.getNumber() + 1,  // 페이지 번호는 0부터 시작하므로 +1
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements(),
				page.hasNext(),
				page.hasPrevious()
			);
		}
	}
}
