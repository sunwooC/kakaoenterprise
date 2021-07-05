package com.kakaoenterprise.web.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Valid 예외처리
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidCustomException extends RuntimeException {
	private Error[] errors;

	public ValidCustomException(String defaultMessage, String field) {
		this.errors = new Error[] { new Error(defaultMessage, field) };
	}

	public ValidCustomException(Error[] errors) {
		this.errors = errors;
	}

	public Error[] getErrors() {
		return errors;
	}

	public static class Error {

		private String defaultMessage;
		private String field;

		public Error(String defaultMessage, String field) {
			this.defaultMessage = defaultMessage;
			this.field = field;
		}

		public String getDefaultMessage() {
			return defaultMessage;
		}

		public String getField() {
			return field;
		}
	}
}