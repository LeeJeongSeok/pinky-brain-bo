package com.jeongseok.pinkybrainbo.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	/**
	 * 404 Not Found
	 */
	NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "존재하지 않는 상품 정보입니다.", false),
	NOT_FOUND_PRODUCT_IMAGE(HttpStatus.NOT_FOUND, "존재하지 않는 상품 이미지 정보입니다.", false),
	NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다.", false),

	/**
	 * 500 Internal Server Error
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.", false),
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final boolean isSuccess;
}
