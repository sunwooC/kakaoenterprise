include dockerenv.default

all: copy login build push
	
#### COPY jar from maven ####
copy:
	cp /var/jenkins_home/workspace/maven_test123123/kakaoenterprise/target/kakaoenterpriseproj-0.0.1-SNAPSHOT.jar .

#### login Docker ####
login:
	docker login -u "$(USER)" -p "$(PASSWORD)"

#### build Docker ####
build:
	docker build -t $(NS)/$(IMAGE_NAME):$(VERSION) .

#### push Docker ####
push:
	docker push $(NS)/$(IMAGE_NAME):$(VERSION)

