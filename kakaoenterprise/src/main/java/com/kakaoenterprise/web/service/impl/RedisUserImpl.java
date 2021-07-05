package com.kakaoenterprise.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kakaoenterprise.config.RedisConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis에 로그인 사용자를 등록하고 삭제하는 기능
 * 
 * @author sunwoo cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisUserImpl {
	@Autowired
	private RedisConfig redisConfig;

	public void login(String username, String sessionId) {
		RedisTemplate redisTemplate = redisConfig.redisTemplate();
		redisTemplate.opsForHash().put("LOGIN:USER:ID", username, sessionId);
	}

	public void logout(String username) {
		RedisTemplate redisTemplate = redisConfig.redisTemplate();
		String session = (String) redisTemplate.opsForHash().get("LOGIN:USER:ID", username);
		if (session != null) {
			redisTemplate.delete("spring:session:sessions:" + session);
			redisTemplate.opsForHash().delete("LOGIN:USER:ID", username);
		}
	}
}