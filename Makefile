DOCKER_ID := $(shell docker ps -aq)
DOCKER_IMAGE := $(shell docker images -q)
DOCKER_VOLUME := $(shell docker volume ls -q)

up:
	@make build
	docker compose up --build -d

down:
	docker compose down

compile:
	@./gradlew classes

build:
	@make clean
	@./gradlew build

test:
	@./gradlew test

log:
	docker compose logs

clean:
	@./gradlew clean

fclean:
	@make clean
	$(if $(DOCKER_ID), docker rm -f $(DOCKER_ID))
	$(if $(DOCKER_VOLUME), docker volume rm $(DOCKER_VOLUME))
	$(if $(DOCKER_IMAGE), docker rmi $(DOCKER_IMAGE))

re:
	@make fclean
	@make up

.PHONY: up down compile build test log clean fclean re