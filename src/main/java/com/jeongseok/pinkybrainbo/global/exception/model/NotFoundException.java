package com.jeongseok.pinkybrainbo.global.exception.model;


import com.jeongseok.pinkybrainbo.global.exception.ErrorCode;

public class NotFoundException extends RuntimeException {

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}
}
