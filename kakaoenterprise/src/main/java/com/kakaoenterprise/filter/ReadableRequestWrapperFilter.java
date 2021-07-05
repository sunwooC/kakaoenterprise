package com.kakaoenterprise.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.kakaoenterprise.aspect.ReadableRequestWrapper;

/**
 * 서블릿리스판스를 chain 하는 필터 Body내용을 일력으로 남기는 기능
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
public class ReadableRequestWrapperFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) {
		// Do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ReadableRequestWrapper wrapper = new ReadableRequestWrapper((HttpServletRequest) request);
		chain.doFilter(wrapper, response);
	}

	@Override
	public void destroy() {
		// Do nothing
	}
}