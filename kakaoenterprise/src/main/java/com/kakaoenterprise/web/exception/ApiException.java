package com.kakaoenterprise.web.exception;


import lombok.Getter;

/**
 * API 예외처리를 위한 공통 예외
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Getter
public class ApiException extends RuntimeException {
    private ExceptionEnum error;

    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }

}