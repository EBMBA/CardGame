FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -f /usr/src/common/pom.xml clean install
COPY game-microservice/src /usr/src/app/src  
COPY game-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV GAME_MICROSERVICE_PORT=8083
ENV WALLET_SERVICE_URL=http://host.docker.internal:8082/api/wallets
ENV INVENTORY_SERVICE_URL=http://host.docker.internal:8084/api/inventories
ENV USER_SERVICE_URL=http://host.docker.internal:8087/api/users
ENV CARD_SERVICE_URL=http://host.docker.internal:8085/api/cards
ENV AUTH_SERVICE_URL=http://host.docker.internal:8086/api/auth
COPY --from=build /usr/src/app/target/game-microservice-0.0.1-SNAPSHOT.jar /usr/app/game-microservice-0.0.1-SNAPSHOT.jar  

EXPOSE 8083
ENTRYPOINT ["java","-jar","/usr/app/game-microservice-0.0.1-SNAPSHOT.jar"]