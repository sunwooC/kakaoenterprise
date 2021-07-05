package com.kakaoenterprise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import lombok.RequiredArgsConstructor;

/**
 * Redis에 대한 설정 정보 및 RedisTemplate을 일관대게 설정하는 기능
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 120) /* 세션 만료 시간 : 60초 */
@RequiredArgsConstructor
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(password);
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		return lettuceConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		/**
		 * 2017-11-15 (고병만)수정 필수처리 key StringRedisSerializer 처리를 해줘야 key앞에 이상한 특수 문자들이
		 * 임의로 붙는 상황을 막을 수 있음 ex)\xAC\xED\x00\x05t\x00\x12
		 */
		template.setKeySerializer(new StringRedisSerializer());
		return template;
	}
}