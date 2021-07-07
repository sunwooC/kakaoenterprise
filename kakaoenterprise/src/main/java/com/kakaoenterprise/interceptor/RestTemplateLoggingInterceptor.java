package com.kakaoenterprise.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
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
		HttpHeaders headers = request.getHeaders();
		UUID logKey = UUID.randomUUID();
		headers.add("LOGKEY", logKey.toString());
		loggingRequest(request, body);
		ClientHttpResponse response = null;
		try {
			response = execution.execute(request, body);
		} catch (Exception e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("http_method", request.getMethod());
			jsonObject.put("request_uri", request.getURI());
			jsonObject.put("heder", request.getHeaders());
			jsonObject.put("body", IOUtils.toString(body, StandardCharsets.UTF_8.name()));
			jsonObject.put("marker", marker);
			jsonObject.put("error", e.toString());
			log.error("{'EXT_REQ_ERR':{}}", e.toString());
			throw e;
		}
		loggingResponse(request, response);
		return response;
	}

	private void loggingRequest(HttpRequest request, byte[] body) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("http_method", request.getMethod());
		jsonObject.put("request_uri", request.getURI());
		jsonObject.put("heder", request.getHeaders());
		jsonObject.put("body", IOUtils.toString(body, StandardCharsets.UTF_8.name()));
		jsonObject.put("marker", marker);
		log.info("{'INT_REQ':{}}", jsonObject);
	}

	private void loggingResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("http_method", request.getMethod());
		jsonObject.put("status", response.getBody());
		jsonObject.put("request_uri", request.getURI());
		jsonObject.put("heder", response.getHeaders());
		jsonObject.put("body", IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name()));
		jsonObject.put("marker", marker);
		log.info("{'INT_RES':{}}", jsonObject);
	}

}