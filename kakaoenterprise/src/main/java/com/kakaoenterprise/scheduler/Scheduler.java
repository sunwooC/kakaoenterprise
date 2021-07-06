package com.kakaoenterprise.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	//초 분 시 일 월 주 년
   @Scheduled(cron = "0 0/10 * * * *")
   public void cronJobSchㅠSpringActuator() {
	   //Spring Actuator
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
      Date now = new Date();
      String strDate = sdf.format(now);
      System.out.println("Java cron job expression:: " + strDate);
   }
}