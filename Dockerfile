FROM maven:3.3-jdk-8 as BUILDER
COPY src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package

FROM openjdk:8-jdk-alpine
EXPOSE 8080
COPY --from=BUILDER target/homework-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=prod -jar /app.jar"]