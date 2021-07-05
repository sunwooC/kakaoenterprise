package com.kakaoenterprise.web.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.kakaoenterprise.config.RestConfig;

import org.springframework.util.LinkedMultiValueMap;

import com.kakaoenterprise.web.dto.KaKaoUserInfo;
import com.kakaoenterprise.web.dto.KakaoAuthToken;

import lombok.extern.slf4j.Slf4j;

	

/**
 * 카카오 사용자 정보 조회,로그아웃,로그인 기능 담당
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@SpringBootTest
public class KakaoServiceImplTest {
	
	@Autowired
	KakaoServiceImpl kakaoServiceImpl;


	@Test
	public void postUserMeAdminTset() {
		//외부 연계튼 테스트 스깁
		//ResponseEntity<String> info = kakaoServiceImpl.postUserMeAdmin("1780872343");
		//assertEquals("11", "11");
	}
	@Test
	public void updateTokenTest() {
		//외부 연계튼 테스트 스깁
		//assertEquals("11", "11");
	}
	@Test
	public void unlinkAdminTest() {
		//외부 연계튼 테스트 스깁
		//assertEquals("11", "11");
	}
	@Test
	public void logoutAdminTest() {
		//외부 연계튼 테스트 스깁
		//assertEquals("11", "11");
	}
	@Test
	public void  logoutTest() {
		//외부 연계튼 테스트 스깁
		//assertEquals("11", "11");
	}
	
}
