FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -X -f /usr/src/common/pom.xml clean install
COPY wallet-microservice/src /usr/src/app/src  
COPY wallet-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV WALLET_MICROSERVICE_PORT=8080
ENV USER_SERVICE_URL=http://host.docker.internal:8087/api/users
COPY --from=build /usr/src/app/target/wallet-microservice-0.0.1-SNAPSHOT.jar /usr/app/wallet-microservice-0.0.1-SNAPSHOT.jar  
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/wallet-microservice-0.0.1-SNAPSHOT.jar"]