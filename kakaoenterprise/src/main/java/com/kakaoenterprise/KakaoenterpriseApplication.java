package com.kakaoenterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class KakaoenterpriseApplication {

	public static void main(String[] args) {
		log.info("start");
		SpringApplication.run(KakaoenterpriseApplication.class, args);
	}

}
