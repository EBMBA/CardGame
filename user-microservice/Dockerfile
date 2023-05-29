FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -f /usr/src/common/pom.xml clean install
COPY user-microservice/src /usr/src/app/src  
COPY user-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV USER_MICROSERVICE_PORT=8087
ENV WALLET_SERVICE_URL=http://host.docker.internal:8080/api/wallets
ENV INVENTORY_SERVICE_URL=http://host.docker.internal:8084/api/inventories
ENV JDBC_URL=localhost
ENV JDBC_USER=postgres
ENV JDBC_PASSWORD=postgres
ENV JDBC_DATABASE=cardgame
ENV JDBC_PORT=5432
COPY --from=build /usr/src/app/target/user-microservice-0.0.1-SNAPSHOT.jar /usr/app/user-microservice-0.0.1-SNAPSHOT.jar  
EXPOSE 8087
ENTRYPOINT ["java","-jar","/usr/app/user-microservice-0.0.1-SNAPSHOT.jar"]