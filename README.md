1.문제해결 전략

2.환경 설정
 2.1 mysql 설치 및 설정
 docker pull mysql:8.0.17
 docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password --name mysql_sample mysql
 
 2.2 redis 서버 설치
 docker pull redis:alpine
 docker run -p 6379:6379 --name myredis -vc:/redis:/data -d redis:alpine redis-server --appendonly yes

3.프로젝트 빌드 및 실행
 3.1 git clone
   git clone https://github.com/sunwooC/kakaoenterprise.git
 3.2 maven 빌드
   cd ./kakaoenterprise/kakaoenterprise
   mvnw.cmd install
 3.3 실행
   cd target
   #db 및 redis 정보가 다르다면
   #target폴더의 application.yml 파일을 환경에 맞게 수정
   #spring.datasource.url,username,password
   #spring.redis.host,port,password
   #spring.security.oauth2.client.registration.kakao.redirect-uri
   
   java -jar  kakaoenterpriseproj-0.0.1-SNAPSHOT.jar
 
4.배포 전략
 4.1 docker pull blueballpen/work
   docker run -d -p 8080:8080 -v c:/jenkins:/var/jenkins_home --name jm_jenkins -u root jenkins/jenkins:lts

   #젠킨스 로그인 정보
   admin / c7f4dd47ae684c0ca17845d568126cae

   build_push_docker: 젠킨스가 포함된 도커 
   maven_test123123
   run_maven_docker
   run_mysql_docker
   run_redis_docker
   
   
   
