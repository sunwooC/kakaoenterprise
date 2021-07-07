include dockerenv.default

all: run_redis

#### run Docker_mysql ####
run_redis:
	docker run -p 6379:6379 --name myredis -vc:/redis:/data -d redis:alpine redis-server --appendonly yes

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
