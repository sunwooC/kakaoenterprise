package com.kakaoenterprise.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kakaoenterprise.interceptor.LoggingInterceptor;
import com.kakaoenterprise.interceptor.LoginCheckInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * WebMvcConfigurer 구현브로 공토인터셉터 등록하하는 기능
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
	private final LoggingInterceptor loggingInterceptor;
	private final LoginCheckInterceptor loginCheckInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(loginCheckInterceptor).excludePathPatterns("/css/**", "/js/**", "/login",
				"/login.html");
		registry.addInterceptor(loggingInterceptor).excludePathPatterns("/css/**", "/js/**");
		// registry.addInterceptor(new LoggerInterceptor())

	}
}
