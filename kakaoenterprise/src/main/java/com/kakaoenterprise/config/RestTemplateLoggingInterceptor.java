package com.kakaoenterprise.config;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Marker;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {
    private String marker;
    public RestTemplateLoggingInterceptor(String marker) {
        this.marker = marker;
    }
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        loggingRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
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
    	resLog.append(" Status code     : ").append( response.getStatusCode());
    	resLog.append(" Request Headers : ").append(response.getHeaders());
    	resLog.append(" Response body   : ").append(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name()));
    	log.info(resLog.toString());
    }

}