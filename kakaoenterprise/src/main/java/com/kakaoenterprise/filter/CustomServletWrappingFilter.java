package com.kakaoenterprise.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * http body,헤더 정보를 로깅하기 위한 Filter
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Component
public class CustomServletWrappingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

		chain.doFilter(wrappingRequest, wrappingResponse);

		wrappingResponse.copyBodyToResponse();
	}
}