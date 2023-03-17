AUTH_SERVICE_PATH = ../services/auth-service
BUILD_AUTH_SERVICE = cd $(AUTH_SERVICE_PATH) && make

GATEWAY_SERVICE_PATH = ../services/gateway-service
BUILD_GATEWAY_SERVICE = cd $(GATEWAY_SERVICE_PATH) && make

PLAYER_SERVICE_PATH = ../services/player-service
BUILD_PLAYER_SERVICE = cd $(PLAYER_SERVICE_PATH) && make

MATCH_SERVICE_PATH = ../services/match-service
BUILD_MATCH_SERVICE = cd $(MATCH_SERVICE_PATH) && make

KAFKA_SERVICE_PATH = ../services/kafka-service
BUILD_KAFKA_SERVICE = cd $(KAFKA_SERVICE_PATH) && make

NOTIFICATIONS_SERVICE_PATH = ../services/notifications-service
BUILD_NOTIFICATIONS_SERVICE = cd $(NOTIFICATIONS_SERVICE_PATH) && make

BETA_FRONTEND_PATH = ../frontends/beta-frontend
BUILD_BETA_FRONTEND = cd $(BETA_FRONTEND_PATH) && make

all: build_auth_service build_gateway_service build_player_service build_match_service build_kafka_service build_notifications_service build_beta_frontend

build_auth_service:
	$(BUILD_AUTH_SERVICE)

build_gateway_service:
	$(BUILD_GATEWAY_SERVICE)

build_player_service:
	$(BUILD_PLAYER_SERVICE)

build_match_service:
	$(BUILD_MATCH_SERVICE)

build_kafka_service:
	$(BUILD_KAFKA_SERVICE)

build_notifications_service:
	$(BUILD_NOTIFICATIONS_SERVICE)

build_beta_frontend:
	$(BUILD_BETA_FRONTEND)