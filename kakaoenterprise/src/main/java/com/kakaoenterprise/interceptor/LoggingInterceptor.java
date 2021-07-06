package com.kakaoenterprise.interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		UUID logKey = UUID.randomUUID();
		try {
			HttpSession session = request.getSession(false);
			JSONObject params = new JSONObject();
			params.put("http_method", request.getMethod());
			params.put("request_uri", request.getRequestURI());
			if (session != null) {
				if (session.getAttribute("sessionId") == null) {
					String sessionId = session.getAttribute("sessionId") + "";
					params.put("sessionId", sessionId);
				}
				if (session.getAttribute("sessionUserName") == null) {
					String sessionUserName = session.getAttribute("sessionUserName") + "";
					params.put("sessionUserName", sessionUserName);
				}
				params.put("request_uri", request.getRequestURI());
			}
			params.put("params", getParams(request));
			params.put("heder", geHeader(request, logKey.toString()));
			params.put("body", new String(cachingRequest.getContentAsByteArray(), "utf-8"));
			log.info("{'INT_REQ':{}}", params);
		} catch (Exception ex) {
			log.error("{'INT_LOG_ERR':{}}", ex.fillInStackTrace());
		}
		response.setHeader("LOGKEY", logKey.toString());

		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
		try {
			HttpSession session = request.getSession(false);
			JSONObject params = new JSONObject();
			params.put("http_method", request.getMethod());
			params.put("request_uri", request.getRequestURI());
			params.put("status", response.getStatus());
			if(session != null) {
				if(session.getAttribute("sessionId") == null) {
					String sessionId = session.getAttribute("sessionId")+"";
					params.put("sessionId", sessionId);
				}
				if(session.getAttribute("sessionUserName") == null) {
					String sessionUserName = session.getAttribute("sessionUserName")+"";
					params.put("sessionUserName", sessionUserName);
				}
				params.put("request_uri", request.getRequestURI());
			}
			params.put("params", getParams(request));
			params.put("body", new String(cachingResponse.getContentAsByteArray(), "utf-8"));
			
			log.info("{'INT_RES':{}}",params);
			//log.info("{INT_RES: {}", objectMapper.readTree(cachingResponse.getContentAsByteArray()));
		}catch(Exception ex1) {
			log.error("'INT_LOG_ERR':{}",ex1.fillInStackTrace());
		}
	}

	/**
	 * request 에 담긴 정보를 JSONObject 형태로 반환한다.
	 * 
	 * @param request
	 * @return
	 */
	private static JSONObject getParams(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String replaceParam = param.replaceAll("\\.", "-");
			jsonObject.put(replaceParam, request.getParameter(param));
		}
		return jsonObject;
	}

	private JSONObject geHeader(HttpServletRequest request, String logKey) {
		Enumeration<String> em = request.getHeaderNames();
		JSONObject jsonObject = new JSONObject();
		while (em.hasMoreElements()) {
			String name = em.nextElement();
			String val = request.getHeader(name);
			jsonObject.put(name, val);
		}
		jsonObject.put("LOGKEY", logKey);
		return jsonObject;
	}

	private JSONObject geHeader(HttpServletResponse response) {

		List<String> names = new ArrayList<>(response.getHeaderNames());
		JSONObject jsonObject = new JSONObject();
		for (String name : names) {
			jsonObject.put(name, response.getHeader(name));
		}
		return jsonObject;
	}
}