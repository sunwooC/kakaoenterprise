package com.kakaoenterprise.aspect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * AOP로 Controller에 대해 이력을 남김
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Component
@Aspect
@Slf4j
public class LoggerAspect {
	@Pointcut("execution(* com.kakaoenterprise..*Controller.*(..))") // 이런 패턴이 실행될 경우 수행
	public void loggerPointCut() {
	}

	@Around("loggerPointCut()")
	public Object methodLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		try {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Object result = proceedingJoinPoint.proceed();
			stopWatch.stop();
		
			return result;

		} catch (Throwable throwable) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest(); // request 정보를 가져온다.
			JSONObject params = new JSONObject();
			String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
			String methodName = proceedingJoinPoint.getSignature().getName();
			params.put("http_method", request.getMethod());
			params.put("request_uri", request.getRequestURI());
			params.put("controller", controllerName);
			params.put("method", methodName);
			params.put("params", getParams(request));
			params.put("heder", geHeader(request));
			StringWriter sw = new StringWriter(); throwable.printStackTrace(new PrintWriter(sw));
			params.put("error", sw.toString());
			log.error("{\"INT_REQ_PROC\":'{}' }",params.toJSONString());
			throw throwable;
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

	private JSONObject geHeader(HttpServletRequest request) {
		Enumeration<String> em = request.getHeaderNames();
		JSONObject jsonObject = new JSONObject();
		while (em.hasMoreElements()) {
			String name = em.nextElement();
			String val = request.getHeader(name);
			jsonObject.put(name, val);
		}
		return jsonObject;
	}

}
