# 1. 문제 해결 전략
## <ol>1.1 요구사항
      i. 카카오 REST API 사용
      ii. 일반 사용자 관리 기능
      iii. 배포/패키징
      iv. 로그 및 모니터링
## <ol>1.2 개발 방향
      i. 카카오, 일반사용자 관리를 위해 사용자 통합
         내부ID, 유저명, Email, 닉네임, SNSID, 시스템ID,연령대,refreshToken,accessToekn,
         생성일
      ii. 로그인 화면, 유저목록 화면으로 구성
      iii. 사용자의 시스템ID에 따라 사용 기능 제약
      iv. 로그 및 모니터링을 위한 로그 규직
         내부 : INT 접두어
         외부: EXT 접두어
      v. 배포/패키징을 위해 젠킨스, 도커 선정
## <ol>1.3 개발 방법
      i. 화면 VUE.js CDN 방식
      ii. SpringBoot RestController 사용
      iii. 카카오 연동시 RestTemplate 사용
      iv. 연결끊기 기능을 위해 Redis 세션서버로 사용

# 2. 환경 설정 
## <ol>2.1 mysql 설치 및 설정 
      docker pull mysql:8.0.17  
      docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password --name mysql_sample mysql  
      
      i. mysql database 생성을 위해 접속
      docker exec -it mysql_sample /bin/bash
      mysql -u root -p
      #패스워드 password (도커런시 지정)
      create database work;
      
      ii.테이블 생성(ddl-auto: update이 되어 있어서 안만들어 됨.)
      USE work;
      CREATE TABLE `User` (
            `id` BIGINT(19) NOT NULL AUTO_INCREMENT COMMENT '일련번호',
            `accessToekn` VARCHAR(60) NULL DEFAULT NULL  COMMENT '엑세스토큰 ',
            `agegrop` INT(10) NULL DEFAULT NULL COMMENT '나이그룹',
            `createDate` DATETIME(6) NULL DEFAULT NULL COMMENT '생성일',
            `email` VARCHAR(50) NOT NULL COMMENT 'EMAIL',
            `nickname` VARCHAR(100) NOT NULL  COMMENT '닉네임',
            `password` VARCHAR(128) NULL DEFAULT NULL  COMMENT '패스워드',
            `refreshToken` VARCHAR(60) NULL DEFAULT NULL  COMMENT '리플레쉬토큰',
            `role` VARCHAR(255) NULL DEFAULT NULL COMMENT 'ROLE',
            `snsid` BIGINT(19) NULL DEFAULT NULL COMMENT '카카오 일련번호',
            `sysid` VARCHAR(100) NULL DEFAULT NULL COMMENT '시스템id',
            `username` VARCHAR(100) NOT NULL COMMENT '유저명(로그인id)',
            PRIMARY KEY (`id`) USING BTREE,
            UNIQUE INDEX `UK_User` (`username`) USING BTREE
      )
      ENGINE=InnoDB
      ;      
      
## <ol>2.2 redis 설치  
       docker pull redis:alpine  
       docker run -p 6379:6379 --name myredis -vc:/redis:/data -d redis:alpine redis-server --appendonly yes  

# 3. 프로젝트 빌드 및 실행 
## <ol>3.1 git clone  
      git clone https://github.com/sunwooC/kakaoenterprise.git  
## <ol>3.2 maven 빌드   
      cd ./kakaoenterprise/kakaoenterprise   
      mvnw.cmd install  
## <ol>3.3 실행 
      cd target 
      #db 및 redis 정보가 다르다면  
      #target폴더의 application.yml 파일을 환경에 맞게 수정  
      #spring.datasource.url,username,password  
      #spring.redis.host,port,password  
      #spring.security.oauth2.client.registration.kakao.redirect-uri 
   
      java -jar  kakaoenterpriseproj-0.0.1-SNAPSHOT.jar  
 
# 4. 빌드/배포 전략 
## <ol>4.1 빌드/배포를 위한 젠킨스 도커 생성(4.2에 통합본 사용,설치불필요)
       docker pull jenkins/jenkins:lts  
       docker run -d -p 8081:8081 -v c:/jenkins:/var/jenkins_home --name jm_jenkins -u root jenkins/jenkins:lts  

## <ol>4.2 젠킨스 빌드 환경 구성
      4-1에서 생성한 젠킨스에 도커 및 메이븐 ,Makefile, 빌드 프로젝트까지 통합하여 도커허브에 반영 완료
      
      docker pull blueballpen/work:lates  
      docker run -d -p 8081:8080 -p 50000:50000 -v //var/run/docker.sock:/var/run/docker.sock -v c:/jenkins:/var/jenkins_home --name jenkins3 blueballpen/work:latest  
      #젠킨스 로그인 정보 admin / c7f4dd47ae684c0ca17845d568126cae
      젠키스 내부에 후 [그림-1]과 같이 구성
<img src="https://github.com/sunwooC/kakaoenterprise/blob/master/image3.PNG"  width="700" height="370">

      i. maven_test123123 : 이번 로그인 프로젝트 메이븐 빌드  
      ii. build_push_docker: maven_test123123에서 빌드된 jar로 도커이미지생성
      iii. run_maven_docker: build_push_docker에서 생성된 도커를 실행 
      iv. run_mysql_docker: mysql 실행 
      v. run_redis_docker: redis 실행

      각 빌드 스크립트는 templates/workspace 아래에 반영
 
## <ol>4.3 구조
<img src="https://github.com/sunwooC/kakaoenterprise/blob/master/image1.PNG"  width="700" height="370">
      개발 환경 구조는 [그림1]과 같이 구성되고 배포를 위해 만든 환경은
<img src="https://github.com/sunwooC/kakaoenterprise/blob/master/image2.PNG"  width="700" height="370">
      [그림2]와 같이 구성. 젠킨스내부의 도커도 로컬PC와 공유되지만 app에서는 Redis,Mysql의 IP를  
      알 수 없어 로컬PC의 IP로 호출해 처리하도록 구성해야 사용 가능합니다.

# 5. 로그 관리 및 모니터링
## <ol>5.1 로그 관리
      내부 : INT 접두어
      외부: EXT 접두
      각 접두어의 ERROR레벨 로그 공통으로 남는 클래스는 아래 2개로
      ApiExceptionAdvice.java는 RestContoller에서 발생한 오류
      RestTemplateLoggingInterceptor.java 외부연계(카카오)에서 발생한 오류
      INFO레벨의 로그에는 HTTP요청과 응답에 대한 로그와
      외부연계(카카오)시 사용된 HTTP요청 응답이 포함.
      LoggerAspect에서는 처리 시간이 5초 이상인 요청에 대해 WARN레벨의 로그를 기록.
      
## <ol>5.2 모니터링
      i.내부 : INT 접두어,외부: EXT 접두
      두개의에서 발생한 에러 로는 시스템 장애로 의심할 수 있는 로그로
      INT접두어 ERROR레벨의 경우 DB, Redis등의 장애와 
      ,EXT접두어 ERROR레벨의 경우 카카오 연동 관련 장애로 의삼할 수 있음. 
      INT접두어 WARN레벨의 경우 LoggerAspect에서 5초 이상 수행된 경우 남기고 있어
      이 로그를 수집하여 개선 가능하지 검토 필요.
      
      ii.모니터링스퀘줄러 :  MNR 접두어
      Scheduler.java에서 남기게 되는 로그로 
      5분 주기로 어플리케이션의 상태를 actuator를 통해 서버 상태를 확인함.
      해당 MNR에 ERROR이 발생했다면 반드시 확인이 필요함
      (현재는 아주 간단하게 개발되어 있으며, 실사용을 위해서는 해당 부분 분리된 서버에서 수행필요)
      
