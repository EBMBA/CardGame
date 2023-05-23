FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -f /usr/src/common/pom.xml clean install
COPY auth-microservice/src /usr/src/app/src  
COPY auth-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV AUTH_MICROSERVICE_PORT=8086
ENV USER_SERVICE_URL=http://host.docker.internal:8087/api/users
COPY --from=build /usr/src/app/target/auth-microservice-0.0.1-SNAPSHOT.jar /usr/app/auth-microservice-0.0.1-SNAPSHOT.jar  
EXPOSE 8086
ENTRYPOINT ["java","-jar","/usr/app/auth-microservice-0.0.1-SNAPSHOT.jar"]