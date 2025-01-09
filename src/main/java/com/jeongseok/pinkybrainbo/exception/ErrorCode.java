package com.jeongseok.pinkybrainbo.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다.", false),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.", false),
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final boolean isSuccess;
}
