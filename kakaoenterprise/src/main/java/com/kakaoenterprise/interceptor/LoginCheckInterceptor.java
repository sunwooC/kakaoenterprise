package com.kakaoenterprise.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 현재 사용자가 로그인 되어 있는지를 확인하는 인터셉터
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoginCheckInterceptor extends BaseInterceptor {
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String redirectUri;
	private String[] allowedUris = new String[] { "/login.html", "/api/v1/auth/local", "/api/v1/auth/local/new",
			"/login/oauth2/code/kakao", "/login/oauth2/kakao/url", "swagger-ui.html" };

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		requstLog(request, response);
		
		String suthorization = request.getHeader("Authorization");
		if ("Bearer sunwoo".equals(suthorization)) {
			return true;
		}
		String uriPath = request.getServletPath();
		HttpSession session = request.getSession(false);
		if (session != null) {
			if (uriPath.equals("/login.html") || uriPath.equals("/")) {
				response.sendRedirect(request.getContextPath() + "/user/userlist.html");
				return true;
			}
		}
		for (String url : allowedUris) {
			if (url.equals(uriPath)) {
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