package com.kakaoenterprise.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;
	private String[] allowedUris = new String[] { "/login.html","/api/v1/auth/local" ,"/api/auth/local/new","/login/oauth2/code/kakao"};

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uriPath = request.getServletPath();
		HttpSession session = request.getSession(false);
		if(session != null) {
			if(uriPath.equals("/login.html")) {
				response.sendRedirect(request.getContextPath() + "/user/userlist.html");
			}
		}
		for(String url : allowedUris ) {
			if(url.equals(uriPath)) {
				return true;
			}
		}
		
		
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/login.html");
			return false;
		}
		return true;
	}
}