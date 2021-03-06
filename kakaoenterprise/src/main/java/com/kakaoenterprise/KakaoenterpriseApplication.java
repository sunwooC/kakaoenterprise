package com.kakaoenterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

/**
 * 메인 함수가 있는 곳으로 프로그램 시작접
 * 
 * @author sunwo.cho
 * @date 2021.07.05
 * @version 1.0
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
public class KakaoenterpriseApplication {
	/**
	 * 메인 함수가 있는 곳으로 프로그램 시작접
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(KakaoenterpriseApplication.class, args);
	}

}
