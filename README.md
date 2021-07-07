# 1.문제해결 전략  
## 1.1 

# 2.환경 설정 
## <ol>2.1 mysql 설치 및 설정 
   docker pull mysql:8.0.17  
   docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password --name mysql_sample mysql  
## <ol>2.2 redis 서버 설치  
 docker pull redis:alpine  
 docker run -p 6379:6379 --name myredis -vc:/redis:/data -d redis:alpine redis-server --appendonly yes  

# 3.프로젝트 빌드 및 실행 
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
 
# 4.배포 전략 
## <ol>4.1 배포를 위한 젠킨스 도커 생성 
 docker pull jenkins/jenkins:lts  
 docker run -d -p 8081:8081 -v c:/jenkins:/var/jenkins_home --name jm_jenkins -u root jenkins/jenkins:lts  

## <ol>4.2 젠킨스 빌드 
 
   docker pull blueballpen/work:lates  
   docker run -d -p 8081:8080 -p 50000:50000 -v //var/run/docker.sock:/var/run/docker.sock -v c:/jenkins:/var/jenkins_home --name jenkins3 blueballpen/work:latest  

   #젠킨스 로그인 정보  
    admin / c7f4dd47ae684c0ca17845d568126cae   

   build_push_docker: 젠킨스가 포함된 도커를 도커서버에 푸쉬  
   maven_test123123: 이번 로그인 프로젝트 빌드  
   run_maven_docker: maven_test123123에서 빌드된  jar로 도커이미지 생성 후 실행  
   run_mysql_docker: mysql 도커 설치  
   run_redis_docker: redis 도커 설치  
 
## <ol>4.3 구조
   ![ex_screenshot](image1.png)
    
 
   
   
   
