package com.kakaoenterprise.web.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class ApiExceptionEntity {
    private String errorCode;
    private String message;

    @Builder
    public ApiExceptionEntity(HttpStatus status, String errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }
}