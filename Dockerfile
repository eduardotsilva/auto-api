FROM openjdk:21-slim

WORKDIR /app

COPY target/auto-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
EXPOSE 5001

ENTRYPOINT ["java", "-jar", "app.jar"] 