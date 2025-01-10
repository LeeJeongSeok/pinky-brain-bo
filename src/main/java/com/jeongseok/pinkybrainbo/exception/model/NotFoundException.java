package com.jeongseok.pinkybrainbo.exception.model;


import com.jeongseok.pinkybrainbo.exception.ErrorCode;

public class NotFoundException extends RuntimeException {

	public NotFoundException(ErrorCode errorCode) {
		super(errorCode.getMessage());
	}
}
