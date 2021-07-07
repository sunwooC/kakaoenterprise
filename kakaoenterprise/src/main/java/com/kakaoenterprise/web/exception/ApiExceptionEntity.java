package com.kakaoenterprise.web.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * API 예외처리를 위한 공통 예외 Entity
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Getter
@ToString
public class ApiExceptionEntity {
	private String errorCode;
	private String message;

	@Builder
	public ApiExceptionEntity(HttpStatus status, String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}