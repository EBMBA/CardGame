FROM --platform=linux/amd64 maven:3-jdk-11 AS build 
COPY common/src /usr/src/common/src  
COPY common/pom.xml /usr/src/common  
RUN mvn -f /usr/src/common/pom.xml clean install
COPY card-microservice/src /usr/src/app/src  
COPY card-microservice/pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM --platform=linux/amd64 gcr.io/distroless/java
ENV CARD_MICROSERVICE_PORT=8085
ENV JDBC_URL=localhost
ENV JDBC_USER=postgres
ENV JDBC_PASSWORD=postgres
ENV JDBC_DATABASE=cardgame
ENV JDBC_PORT=5432
COPY --from=build /usr/src/app/target/card-microservice-0.0.1-SNAPSHOT.jar /usr/app/card-microservice-0.0.1-SNAPSHOT.jar  
EXPOSE 8085
ENTRYPOINT ["java","-jar","/usr/app/card-microservice-0.0.1-SNAPSHOT.jar"]