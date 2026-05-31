FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw && ./mvnw -q package -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.23

WORKDIR /app

COPY --from=build /app/target/stt-backend-0.0.1-SNAPSHOT.jar app.jar
COPY ca.pem ca.pem

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
