package com.kakaoenterprise.web.service.impl;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import com.kakaoenterprise.config.RedisConfig;
import com.kakaoenterprise.web.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RedisUserImplTest {
	@Autowired
	private ApplicationContext context;
	

	@Mock
	private RedisTemplate redisTemplate;

	private RedisUserImpl redisUserImpl;
	@BeforeEach
	public void beforeEach() {
		//redisUserImpl = new RedisUserImpl(redisTemplate);
	}
	
	@Test
	public void loginTest() throws Exception {
		redisUserImpl.login("id", "passwrd");
		
		/*
		RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate();
		redisTemplate.opsForHash().delete("LOGIN:USER:ID", "11");
		redisUserImpl.login("11", "11");
		String out = (String) redisTemplate.opsForHash().get("LOGIN:USER:ID", "11");
		assertEquals("11", out);
		*/
	}

	@Test
	public void logoutTest() throws Exception {
		String sessionId = "sessionId:12345";
		given(redisTemplate.opsForHash().get("LOGIN:USER:ID", "abc")).willReturn(sessionId);
		/*
		redisUserImpl.logout("11");
		
		RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate();
		String out = (String) redisTemplate.opsForHash().get("LOGIN:USER:ID", "11");
		assertNull(out);
		*/
	}

}