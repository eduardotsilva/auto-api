# Stage 1: Build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:21-slim
WORKDIR /app

# Copiar o JAR gerado com a versão correta do pom.xml
COPY --from=build /app/target/*.jar app.jar

ENV DATABASE_URL=""
ENV PGUSER=""
ENV PGPASSWORD=""
ENV PORT="8080"

EXPOSE 8080
EXPOSE 5001

ENTRYPOINT ["java", "-jar", "app.jar"] 