package com.kakaoenterprise.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 현재 사용자가 로그인 되어 있는지를 확인하는 인터셉터
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {
	private String marker;

	public RestTemplateLoggingInterceptor(String marker) {
		this.marker = marker;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		loggingRequest(request, body);
		ClientHttpResponse response = null;
		try {
			response = execution.execute(request, body);
		} catch (Exception e) {
			StringBuilder reqLog = new StringBuilder();
			reqLog.append("[REQUEST] Maker : ").append(marker);
			reqLog.append(" URI / Method   : ").append(request.getURI()).append("/").append(request.getMethod());
			reqLog.append(" Method         : ").append(request.getMethod());
			reqLog.append(" Headers        : ").append(request.getHeaders());
			reqLog.append(" Request body   : ").append(IOUtils.toString(body, StandardCharsets.UTF_8.name()));
			reqLog.append(" Error          : ").append(e.toString());
			log.error(reqLog.toString());
			throw e;
		}
		loggingResponse(request, response);
		return response;
	}

	private void loggingRequest(HttpRequest request, byte[] body) throws IOException {
		StringBuilder reqLog = new StringBuilder();
		reqLog.append("[REQUEST] Maker : ").append(marker);
		reqLog.append(" URI / Method   : ").append(request.getURI()).append("/").append(request.getMethod());
		reqLog.append(" Method         : ").append(request.getMethod());
		reqLog.append(" Headers        : ").append(request.getHeaders());
		reqLog.append(" Request body   : ").append(IOUtils.toString(body, StandardCharsets.UTF_8.name()));
		log.info(reqLog.toString());
	}

	private void loggingResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
		StringBuilder resLog = new StringBuilder();
		resLog.append("[RESPONSE] Maker : ").append(marker);
		resLog.append(" URI / Method    : ").append(request.getURI()).append("/").append(request.getMethod());
		resLog.append(" Method          : ").append(request.getMethod());
		resLog.append(" Status code     : ").append(response.getStatusCode());
		resLog.append(" Request Headers : ").append(response.getHeaders());
		resLog.append(" Response body   : ")
				.append(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name()));
		log.info(resLog.toString());
	}

}