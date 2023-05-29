FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -f /usr/src/common/pom.xml clean install
COPY inventory-microservice/src /usr/src/app/src  
COPY inventory-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV INVENTORY_MICROSERVICE_PORT=8084
ENV USER_SERVICE_URL=http://host.docker.internal:8087/api/users
ENV CARD_SERVICE_URL=http://host.docker.internal:8085/api/cards
ENV JDBC_URL=localhost
ENV JDBC_USER=postgres
ENV JDBC_PASSWORD=postgres
ENV JDBC_DATABASE=cardgame
ENV JDBC_PORT=5432
COPY --from=build /usr/src/app/target/inventory-microservice-0.0.1-SNAPSHOT.jar /usr/app/inventory-microservice-0.0.1-SNAPSHOT.jar  
EXPOSE 8084
ENTRYPOINT ["java","-jar","/usr/app/inventory-microservice-0.0.1-SNAPSHOT.jar"]