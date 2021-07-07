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
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BaseInterceptor extends HandlerInterceptorAdapter{
	
	protected void requstLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
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
			params.put("body", new String(request.getContentAsByteArray(), "utf-8"));
			log.info("{\"INT_REQ\":{}}", params.toJSONString());
		} catch (Exception ex) {
			log.error("{\"INT_LOG_ERR\":{}}", exceptionToString(ex));
		}
		response.setHeader("LOGKEY", logKey.toString());
	}
	protected void reqposeLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
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
			params.put("body", new String(response.getContentAsByteArray(), "utf-8"));
			
			log.info("{\"INT_RES\":{}}",params.toJSONString());
			//log.info("{INT_RES: {}", objectMapper.readTree(cachingResponse.getContentAsByteArray()));
		}catch(Exception ex1) {
			log.error("{\"INT_LOG_ERR\":{}}",exceptionToString(ex1));
		}
	}
	/**
	 * request 에 담긴 정보를 JSONObject 형태로 반환한다.
	 * 
	 * @param request
	 * @return
	 */
	protected  JSONObject getParams(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String replaceParam = param.replaceAll("\\.", "-");
			jsonObject.put(replaceParam, request.getParameter(param));
		}
		return jsonObject;
	}

	protected  JSONObject geHeader(HttpServletRequest request, String logKey) {
		Enumeration<String> em = request.getHeaderNames();
		JSONObject jsonObject = new JSONObject();
		while (em.hasMoreElements()) {
			String name = em.nextElement();
			String val = request.getHeader(name);
			jsonObject.put(name, val);
		}
		return jsonObject;
	}

	protected  JSONObject geHeader(HttpServletResponse response) {

		List<String> names = new ArrayList<>(response.getHeaderNames());
		JSONObject jsonObject = new JSONObject();
		for (String name : names) {
			jsonObject.put(name, response.getHeader(name));
		}
		return jsonObject;
	}
	protected String exceptionToString(Exception e) {    
		StringWriter error = new StringWriter();        
		e.printStackTrace(new PrintWriter(error));   
		return error.toString();            
	}
}
