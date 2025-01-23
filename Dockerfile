FROM maven:3.8.6-openjdk-18 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -X

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/village-0.0.1-SNAPSHOT.jar app.jar

RUN chmod +x /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
