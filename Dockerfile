FROM openjdk:21-slim

WORKDIR /app

COPY target/auto-api-0.0.2.jar app.jar

# Não precisamos definir as variáveis de ambiente aqui, pois o Railway as fornece
EXPOSE 8080
EXPOSE 5001

ENTRYPOINT ["java", "-jar", "app.jar"] 