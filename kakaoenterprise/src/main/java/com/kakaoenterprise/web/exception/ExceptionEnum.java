package com.kakaoenterprise.web.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

/**
 * API 코드 정릴 enum
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Getter
@ToString
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
    SECURITY_01(HttpStatus.UNAUTHORIZED, "S0001", "권한이 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "S0002", "사용자를 찾을 수 없습니다."),
    NOT_KAKO_USER(HttpStatus.NOT_FOUND, "S0003", "카카오 사용자가 아닙니다."),
    NOT_MATCHED_USER(HttpStatus.BAD_REQUEST, "S0004", "passwd가 일치 하지 않습니다."),
	NOT_JOIN_USER(HttpStatus.BAD_REQUEST, "S0005", "같은 ID가 존재합니다."); 
    
    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}