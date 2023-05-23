FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -f /usr/src/common/pom.xml clean install
COPY store-microservice/src /usr/src/app/src  
COPY store-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV STORE_MICROSERVICE_PORT=8082
ENV WALLET_SERVICE_URL=http://host.docker.internal:8082/api/wallets
ENV INVENTORY_SERVICE_URL=http://host.docker.internal:8084/api/inventories
ENV USER_SERVICE_URL=http://host.docker.internal:8087/api/users
ENV CARD_SERVICE_URL=http://host.docker.internal:8085/api/cards
COPY --from=build /usr/src/app/target/store-microservice-0.0.1-SNAPSHOT.jar /usr/app/store-microservice-0.0.1-SNAPSHOT.jar  
EXPOSE 8082
ENTRYPOINT ["java","-jar","/usr/app/store-microservice-0.0.1-SNAPSHOT.jar"]