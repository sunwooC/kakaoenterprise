package com.kakaoenterprise.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ContentCachingRequestWrapper로 request,response를 변환해 로깅하는 기능
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingInterceptor extends HandlerInterceptorAdapter {

	private final ObjectMapper objectMapper;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		/*
		 * final ContentCachingRequestWrapper cachingRequest =
		 * (ContentCachingRequestWrapper) request; final ContentCachingResponseWrapper
		 * cachingResponse = (ContentCachingResponseWrapper) response;
		 * log.info("Request Body : {}",
		 * objectMapper.readTree(cachingRequest.getContentAsByteArray()));
		 * log.info("Response Body : {}",
		 * objectMapper.readTree(cachingResponse.getContentAsByteArray()));
		 */
	}
}