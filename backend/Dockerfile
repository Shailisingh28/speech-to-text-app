FROM eclipse-temurin:21-jre-alpine-3.23

WORKDIR /app

COPY target/stt-backend-0.0.1-SNAPSHOT.jar app.jar
COPY ca.pem ca.pem

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]