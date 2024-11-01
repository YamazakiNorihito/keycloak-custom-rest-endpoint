FROM maven:3.8.5-openjdk-17-slim
WORKDIR /app
COPY custom-rest-api ./custom-rest-api
WORKDIR /app/custom-rest-api
RUN mvn clean package