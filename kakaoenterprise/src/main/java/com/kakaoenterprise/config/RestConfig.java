package com.kakaoenterprise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.kakaoenterprise.interceptor.RestTemplateLoggingInterceptor;

/**
 * RestTemplate에 대한 설정 및 인스턴스 반환 기능
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Configuration
public class RestConfig {

	public RestTemplate restTemplate(String maker) {
		RestTemplate restTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(5000); // 5초
		factory.setReadTimeout(5000); // 5초
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
		restTemplate.setInterceptors(Lists.newArrayList(new RestTemplateLoggingInterceptor(maker)));
		return restTemplate;
	}
}