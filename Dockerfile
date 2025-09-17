# Etapa 1: Construcción con Maven
FROM maven:3.9.3-eclipse-temurin-20 AS build

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar archivos de proyecto
COPY pom.xml .
COPY src ./src

# Construir el JAR sin ejecutar tests
# RUN mvn clean package -DskipTests
RUN mvn clean package -Dmaven.test.skip=true

# Etapa 2: Imagen ligera para ejecutar la app
FROM eclipse-temurin:20-jre

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java","-jar","app.jar"]