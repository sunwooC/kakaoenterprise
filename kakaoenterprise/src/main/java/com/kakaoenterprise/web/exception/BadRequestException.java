package com.kakaoenterprise.web.exception;

public class BadRequestException extends RuntimeException {
    private String errorCode;

    /**
     * default 400 Error
     */
    public BadRequestException() {
        this.errorCode = "=========================";
    }

    /**
     * Error Code 지정
     * @param code
     */
    public BadRequestException(String code) {
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }
}