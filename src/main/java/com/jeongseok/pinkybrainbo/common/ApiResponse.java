package com.jeongseok.pinkybrainbo.common;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public record ApiResponse<T>(

	HttpStatus httpStatus,
	boolean success,
	@Nullable T data,
	@Nullable ExceptionDto error,
	@Nullable PageInfo pageInfo
) {

	public static <T> ApiResponse<T> ok(@Nullable final T data) {
		return new ApiResponse<>(HttpStatus.OK, true, data, null, null);
	}

	public static <T> ApiResponse<T> created(@Nullable final T data) {
		return new ApiResponse<>(HttpStatus.CREATED, true, data, null, null);
	}

	public static <T> ApiResponse<T> fail(final CustomException e) {
		return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()), null);
	}

	// 페이징 전용 정적 메서드 추가
	public static <T> ApiResponse<T> okWithPaging(@Nullable final T data, final Page<?> page) {
		PageInfo pageInfo = PageInfo.of(page);
		return new ApiResponse<>(HttpStatus.OK, true, data, null, pageInfo);
	}

	// 페이지 정보를 담는 내부 레코드
	public record PageInfo(
		int currentPage,
		int pageSize,
		int totalPages,
		long totalElements,
		boolean hasNext,
		boolean hasPrevious
	) {
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
