package com.kakaoenterprise.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
		JSONArray reqParamMap = new JSONArray();

		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = (String) paramNames.nextElement();
			String value = request.getParameter(key);
			JSONObject item = new JSONObject();
			item.put(key, value);
			reqParamMap.add(item);
		}

		JSONObject reqHederMap = new JSONObject();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			reqHederMap.put(key, value);
		}

		log.info("Request URI :{} ", request.getRequestURI());
		log.info("Request param :{} ", reqParamMap);
		log.info("Request header :{} ", reqHederMap);
		log.info("Request Body : {}", new String(cachingRequest.getContentAsByteArray(), "utf-8"));
		UUID key = UUID.randomUUID();
		response.setHeader("LOGKY", key.toString());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
		final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
		/*
		 * log.info("Request Body : {}",
		 * objectMapper.readTree(cachingRequest.getContentAsByteArray()));
		 */
		log.info("Response Body : {}", objectMapper.readTree(cachingResponse.getContentAsByteArray()));
	}

}