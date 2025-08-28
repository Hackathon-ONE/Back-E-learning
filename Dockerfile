# Build stage
FROM maven:3.9.11-amazoncorretto-17 AS build
WORKDIR /app

# Copiar todo el proyecto
COPY pom.xml .
COPY src ./src

# Construir el jar
RUN mvn clean package -DskipTests

# Run stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]