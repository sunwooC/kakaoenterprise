package com.kakaoenterprise.web.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import com.kakaoenterprise.config.RedisConfig;

@SpringBootTest
class RedisUserImplTest {
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	RedisUserImpl redisUserImpl;
	@Autowired
	RedisConfig redisConfig;
	
	@Test
	public void loginTest() throws Exception {
		RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate();
		redisTemplate.opsForHash().delete("LOGIN:USER:ID", "11");
		redisUserImpl.login("11", "11");
		String out = (String) redisTemplate.opsForHash().get("LOGIN:USER:ID", "11");
		assertEquals("11", out);
	}

	@Test
	public void logoutTest() throws Exception {
		RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate();
		redisUserImpl.logout("11");
		String out = (String) redisTemplate.opsForHash().get("LOGIN:USER:ID", "11");
		assertNull(out);
	}

}