package com.kakaoenterprise.advice;


import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kakaoenterprise.web.controll.WebLoginController;
import com.kakaoenterprise.web.exception.ApiException;
import com.kakaoenterprise.web.exception.ApiExceptionEntity;
import com.kakaoenterprise.web.exception.ExceptionEnum;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;

/**
 * API 예외처리를 위한 공통 예외 Advice
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice(basePackages="com.kakaoenterprise.web")
public class ApiExceptionAdvice {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final ApiException e) {
    	log.error("{\"INT_PROC_ERR\":{}}",exceptionToString(e));
        return ResponseEntity
                .status(e.getError().getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(e.getError().getCode())
                        .message(e.getError().getMessage())
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
    	log.error("{\"INT_PROC_ERR500\":{}}",exceptionToString(e));
        return ResponseEntity
                .status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {
    	log.error("{\"INT_PROC_ERR500\":{}}",exceptionToString(e));
        return ResponseEntity
                .status(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiExceptionEntity> exceptionHandler(HttpServletRequest request, final Exception e) {
    	log.error("{\"INT_PROC_ERR500\":{}}",exceptionToString(e));
        return ResponseEntity
                .status(ExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiExceptionEntity.builder()
                        .errorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                        .message(e.getMessage())
                        .build());
    }
    
	public String exceptionToString(Exception e) {    
		StringWriter error = new StringWriter();        
		e.printStackTrace(new PrintWriter(error));   
		JSONObject obnj = new JSONObject(error);
		return obnj.toString();            
	}
}