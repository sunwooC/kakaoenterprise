package com.kakaoenterprise.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Scheduler {
	// 초 분 시 일 월 주 년
	@Scheduled(cron = "0 0/5 * * * *")
	public void cronJobSchㅠSpringActuator() {
		// Spring Actuator
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		final String uri = "http://localhost:8082/actuator/health";
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.getForObject(uri, String.class);
			log.info("\"MNR\":{}", result);
		} catch (Exception ex) {
			log.error("\"MNR\":{}", ex);

		}
	}
}