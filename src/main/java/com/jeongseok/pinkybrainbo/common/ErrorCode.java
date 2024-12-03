package com.jeongseok.pinkybrainbo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
	INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

	private final Integer code;
	private final HttpStatus httpStatus;
	private final String message;
}
