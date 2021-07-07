include dockerenv.default

all: run_mysql

#### run Docker_mysql ####
run_mysql:
	docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password --name mysql_sample mysql

#### run Docker_app ####
run_app:
	docker run -it --name $(CONTAINER_NAME) $(PORTS) $(NS)/$(IMAGE_NAME):$(VERSION)

#### exec Docker ####
exec:
	docker exec -it $(CONTAINER_NAME) bash

#### stop Docker ####
stop:
	docker stop $(CONTAINER_NAME)
